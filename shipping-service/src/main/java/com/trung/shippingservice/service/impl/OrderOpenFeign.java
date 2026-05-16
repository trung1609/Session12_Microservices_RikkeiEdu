package com.trung.shippingservice.service.impl;

import com.trung.shippingservice.dto.ApiResponse;
import com.trung.shippingservice.dto.OrderDetailResponse;
import com.trung.shippingservice.exception.ResourceNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("ORDER-SERVICE")
public interface OrderOpenFeign {

    @GetMapping("/api/v1/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrderDetail(@PathVariable Long orderId) throws ResourceNotFoundException;
}
