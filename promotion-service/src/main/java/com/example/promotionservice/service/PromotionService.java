package com.example.promotionservice.service;

import com.example.promotionservice.dto.PriceUpdateRequest;
import com.example.promotionservice.entity.Promotion;
import com.example.promotionservice.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionService {
    private final StringRedisTemplate redisTemplate;
    private final PromotionRepository promotionRepository;
    private static final String PROMOTION_CHANNEL = "promotion-updates";

    @Transactional
    public void updatePromotion(Long productId, PriceUpdateRequest request){
        Optional<Promotion> existingPromotion = promotionRepository.findByProductId(productId);
        Promotion promotion;
        if (existingPromotion.isPresent()) {
            promotion = existingPromotion.get();
            promotion.setDiscountedPrice(request.getNewPrice());
            log.info("Cập nhật giá khuyến mãi cho sản phẩm ID: {}", productId);
        } else {
            // Nếu chưa có -> Tạo mới record
            promotion = new Promotion();
            promotion.setProductId(productId);
            promotion.setDiscountedPrice(request.getNewPrice());
            log.info("Tạo mới chương trình khuyến mãi cho sản phẩm ID: {}", productId);
        }

        promotionRepository.save(promotion);

        redisTemplate.convertAndSend(PROMOTION_CHANNEL, String.valueOf(productId));
        log.info("Đã gửi yêu cầu xóa cache cho sản phẩm {} lên channel {}", productId, PROMOTION_CHANNEL);
    }

    public Double getDiscountedPrice(Long productId) {
        return promotionRepository.findByDiscountedPrice(productId);
    }
}
