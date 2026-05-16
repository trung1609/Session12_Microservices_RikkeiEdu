package com.example.flashsaleservice.dto;

import lombok.Data;

@Data
public class BuyNowRequest {
    private Long customerId;
    private Long productId;
    private Integer quantity;
    private Double price;
}
