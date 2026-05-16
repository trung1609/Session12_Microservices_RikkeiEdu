package com.trung.shippingservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequest {

    @NotNull( message = "Product id is required")
    private Long productId;

    @NotBlank( message = "Product name is required")
    private String productName;

    @NotNull( message = "Price is required")
    @PositiveOrZero( message = "Price must be positive or zero")
    private BigDecimal price;

    @NotNull( message = "Quantity is required")
    @PositiveOrZero( message = "Quantity must be positive or zero")
    private Integer quantity;
}
