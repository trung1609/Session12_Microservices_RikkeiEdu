package com.trung.shippingservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingCreateRequest {
    private Long orderId;
    private Long carrierId;
    private List<ShippingItemRequest> items;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShippingItemRequest {
        private Long productId;
        private Integer quantity;
    }
}
