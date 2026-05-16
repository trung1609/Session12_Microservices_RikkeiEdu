package com.example.promotionservice.repository;

import com.example.promotionservice.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> findByProductId(Long productId);

    @Query("SELECT p.discountedPrice FROM Promotion p WHERE p.productId = :productId")
    Double findByDiscountedPrice(Long productId);
}
