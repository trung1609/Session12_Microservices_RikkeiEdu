package com.example.flashsaleservice.service;

import com.example.flashsaleservice.dto.BuyNowRequest;
import com.example.flashsaleservice.event.FlashSaleOrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlashSaleService {
    private final StringRedisTemplate redisTemplate;
    private final RedissonClient redissonClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String KAFKA_TOPIC = "flash-sale-orders";

    public void initiateFlashSaleStock(Long productId, Integer stock){
        String key = "flash-sale-stock:" + productId;
        redisTemplate.opsForValue().set(key, String.valueOf(stock));
        log.info("Flash sale stock for product {} set to {}", productId, stock);
    }

    public String buyNow(BuyNowRequest request) {
        String lockKey = "lock:products::" + request.getProductId();
        String stockKey = "flash-sale-stock:" + request.getProductId();

        RLock lock = redissonClient.getLock(lockKey);
        Boolean isLocked = false;
        try {
            isLocked = lock.tryLock(3, 5, TimeUnit.SECONDS);
            if (!isLocked){
                throw new RuntimeException("Server is busy, please try again later.");
            }

            String currentStockStr = redisTemplate.opsForValue().get(stockKey);
            int currentStock = Integer.parseInt(currentStockStr);
            if (currentStock < request.getQuantity()){
                throw new RuntimeException("Product is out of stock.");
            }

            redisTemplate.opsForValue().decrement(stockKey, request.getQuantity());
            log.info("Product {} has been reduced by {} units", request.getProductId(), request.getQuantity());
            FlashSaleOrderEvent event = FlashSaleOrderEvent.builder()
                    .customerId(request.getCustomerId())
                    .productId(request.getProductId())
                    .quantity(request.getQuantity())
                    .price(request.getPrice())
                    .build();

            kafkaTemplate.send(KAFKA_TOPIC, event);
            log.info("Flash sale order event published for product {}: {}", request.getProductId(), event);
            return "Order placed successfully!";

        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            log.error("Error when trying to acquire lock: {}", e.getMessage());
            throw new RuntimeException("Server is busy, please try again later.");
        }finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
