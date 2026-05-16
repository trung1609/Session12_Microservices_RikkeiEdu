package com.trung.orderservice.service.impl;

import com.trung.orderservice.dto.CustomerResponseDTO;
import com.trung.orderservice.exception.ResourceNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("CUSTOMER-SERVICE")
public interface CustomerClient {
    @GetMapping("/api/v1/customers/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id);
}
