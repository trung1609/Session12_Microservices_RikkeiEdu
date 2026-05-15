package com.trung.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderCreateEvent {
    private Long orderId;
    private Long productId;
    private Long customerId;
    private Integer quantity;
    private Double totalAmount;
    private String userEmail;
}
