package com.trung.orderservice.service.impl;

import com.trung.orderservice.constant.OrderStatus;
import com.trung.orderservice.entity.Orders;
import com.trung.orderservice.event.FlashSaleOrderEvent;
import com.trung.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlashSaleOrderListener {
    private final OrderRepository orderRepository;

    @KafkaListener(topics = "flash-sale-orders", groupId = "flash-sale-group")
    public void processFlashSaleOrder(FlashSaleOrderEvent event){
        log.info("Received flash sale order: {}", event);

        try {
            Orders orders = new Orders();
            orders.setCustomerId(event.getCustomerId());
            orders.setProductId(event.getProductId());
            orders.setTotalAmount(event.getPrice() * event.getQuantity());
            orders.setStatus(OrderStatus.PENDING);
            orderRepository.save(orders);

            log.info("Flash sale order processed successfully");
        }catch (Exception e){
            log.error("Error processing flash sale order: {}", e.getMessage());
        }
    }
}
