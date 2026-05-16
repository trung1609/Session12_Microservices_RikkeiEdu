package com.trung.orderservice.mapper;

import com.trung.orderservice.dto.OrderResponse;
import com.trung.orderservice.entity.Orders;

public class OrderMapper {
    public static OrderResponse toDTO(Orders orders){
        return OrderResponse.builder()
                .id(orders.getId())
                .customerId(orders.getCustomerId())
                .productId(orders.getProductId())
                .totalAmount(orders.getTotalAmount())
                .orderDate(orders.getOrderDate())
                .build();
    }
}
