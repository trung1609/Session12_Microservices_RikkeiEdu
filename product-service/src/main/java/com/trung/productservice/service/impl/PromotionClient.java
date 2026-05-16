package com.trung.productservice.service.impl;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("PROMOTION-SERVICE")
public interface PromotionClient {
    @GetMapping("/api/v1/promotions/products/{productId}/price")
    Double getDiscountedPrice(@PathVariable("productId") Long productId);
}
