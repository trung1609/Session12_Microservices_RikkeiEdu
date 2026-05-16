package com.trung.shippingservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {

    @NotNull( message = "User id is required")
    private Long userId;

    @Valid
    @NotNull( message = "Items is required")
    private List<OrderItemRequest> items;
}
