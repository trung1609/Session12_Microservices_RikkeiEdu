package com.example.promotionservice.controller;

import com.example.promotionservice.dto.PriceUpdateRequest;
import com.example.promotionservice.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @PutMapping("/products/{productId}/price")
    public ResponseEntity<String> updateProductPrice(
            @PathVariable Long productId,
            @RequestBody PriceUpdateRequest request) {

        promotionService.updatePromotion(productId, request);

        return ResponseEntity.ok("Cập nhật giá và gửi yêu cầu xóa cache thành công cho sản phẩm " + productId);
    }

    @GetMapping("products/{productId}/price")
    public ResponseEntity<Double> getDiscountedPrice(@PathVariable Long productId) {
        return ResponseEntity.ok(promotionService.getDiscountedPrice(productId));
    }
}
