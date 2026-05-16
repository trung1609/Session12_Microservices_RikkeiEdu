package com.trung.productservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionUpdateSubscriber {
    private final StringRedisTemplate redisTemplate;

    public void handleMessage(String message) {
        try {
            Long productId = Long.parseLong(message);
            log.info("Nhận được thông báo cập nhật khuyến mãi cho sản phẩm ID: {}", productId);

            // Xoa cache
            String cacheKey = "products::" + productId;
            Boolean isDeleted = redisTemplate.delete(cacheKey);

            if (Boolean.TRUE.equals(isDeleted)) {
                log.info("Đã xóa cache thành công cho sản phẩm có ID: {}", productId);
            } else {
                log.info("Cache không tồn tại để xóa đối với sản phẩm ID: {}", productId);
            }
        } catch (NumberFormatException e) {
            log.error("Lỗi khi xử lý message từ Redis: {}", message, e);
        }
    }
}
