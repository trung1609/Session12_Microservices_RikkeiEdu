package com.trung.orderservice.service.impl;

import com.trung.orderservice.config.RedisConfig;
import com.trung.orderservice.config.RedissonConfig;
import com.trung.orderservice.dto.OrderCreateRequest;
import com.trung.orderservice.dto.OrderResponse;
import com.trung.orderservice.dto.ProductResponseDTO;
import com.trung.orderservice.entity.Orders;
import com.trung.orderservice.event.OrderCreateEvent;
import com.trung.orderservice.exception.InvalidDataException;
import com.trung.orderservice.exception.ResourceNotFoundException;
import com.trung.orderservice.exception.ServerErrorException;
import com.trung.orderservice.mapper.OrderMapper;
import com.trung.orderservice.repository.OrderRepository;
import com.trung.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;
    private final CustomerClient customerClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RedissonConfig redissonConfig;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) throws InvalidDataException, ServerErrorException, ResourceNotFoundException {
        if (customerClient.getCustomerById(request.getCustomerId()).getBody() == null) {
            throw new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId());
        }

        String lockKey = "lock:product:" + request.getProductId();
        RLock lock = redissonConfig.redissonClient().getLock(lockKey);
        boolean isLocked = false;

        try {

            isLocked = lock.tryLock(3, 5, TimeUnit.SECONDS);
            if (!isLocked) {
                log.warn("Don't have the lock for product: {}", request.getProductId());
                throw new ServerErrorException("Server is busy, please try again later.");
            }
            ProductResponseDTO product = productClient.getProductById(request.getProductId());

            if (product.getStockQuantity() < request.getQuantity()) {
                throw new InvalidDataException("Insufficient stock for product id: " + request.getProductId());
            }

            productClient.reduceStock(request.getProductId(), request.getQuantity());

            Orders orders = new Orders();
            orders.setCustomerId(request.getCustomerId());
            orders.setProductId(request.getProductId());
            orders.setTotalAmount(product.getPrice() * request.getQuantity());

            orderRepository.save(orders);

            OrderCreateEvent event = OrderCreateEvent.builder()
                    .orderId(orders.getId())
                    .productId(request.getProductId())
                    .customerId(request.getCustomerId())
                    .totalAmount(orders.getTotalAmount())
                    .quantity(request.getQuantity())
                    .userEmail(customerClient.getCustomerById(request.getCustomerId()).getBody().getEmail())
                    .build();
//            kafkaTemplate.send("order-events", event);

            return OrderMapper.toDTO(orders);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Tiến trình lấy khóa bị gián đoạn", e);
            throw new ServerErrorException("Lỗi hệ thống khi xử lý đồng thời.");
        } finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("Đã giải phóng khóa cho sản phẩm: {}", request.getProductId());
            }
        }
    }

    @Override
    public OrderResponse getOrderById(Long id) throws ResourceNotFoundException {
        return orderRepository.findById(id)
                .map(OrderMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Override
    public ProductResponseDTO getProductFromProductService(Long productId) throws ServerErrorException {
        List<ServiceInstance> instances = discoveryClient.getInstances("PRODUCT-SERVICE");
        if (instances.isEmpty()) {
            throw new ServerErrorException("No instances of PRODUCT-SERVICE found");
        }

        ServiceInstance instance = instances.get(0);

        String targetUrl = "http://PRODUCT-SERVICE/api/v1/products/" + productId;
        return restTemplate.getForObject(targetUrl, ProductResponseDTO.class);
    }
}
