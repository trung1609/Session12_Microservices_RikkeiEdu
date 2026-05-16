package com.trung.orderservice.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreateRequest {
    private Long customerId;
    private Long productId;

    @Min(value = 0, message = "Quantity must be greater than 0")
    private Integer quantity;
}
