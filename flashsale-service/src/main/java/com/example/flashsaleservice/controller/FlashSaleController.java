package com.example.flashsaleservice.controller;

import com.example.flashsaleservice.dto.BuyNowRequest;
import com.example.flashsaleservice.service.FlashSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/flashsales")
@RequiredArgsConstructor
public class FlashSaleController {
    private final FlashSaleService flashSaleService;

    @PostMapping("/init-stock")
    public ResponseEntity<String> initStock(@RequestParam Long productId, @RequestParam int stock) {
        flashSaleService.initiateFlashSaleStock(productId, stock);
        return ResponseEntity.ok("Khởi tạo tồn kho thành công!");
    }

    @PostMapping("/buy")
    public ResponseEntity<String> buyNow(@RequestBody BuyNowRequest request) {
        String message = flashSaleService.buyNow(request);
        return ResponseEntity.ok(message);
    }
}
