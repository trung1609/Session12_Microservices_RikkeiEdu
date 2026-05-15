package com.trung.orderservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Long customerId;
    private Long productId;
    private Double totalAmount;
    private LocalDateTime orderDate;
}
