package com.trung.emailservice.event;

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
    private Double totalAmount;
    private Integer quantity;
    private String userEmail;
}
