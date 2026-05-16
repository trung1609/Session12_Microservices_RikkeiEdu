package com.trung.orderservice.service;

import com.trung.orderservice.dto.OrderCreateRequest;
import com.trung.orderservice.dto.OrderResponse;
import com.trung.orderservice.dto.ProductResponseDTO;
import com.trung.orderservice.exception.InvalidDataException;
import com.trung.orderservice.exception.ResourceNotFoundException;
import com.trung.orderservice.exception.ServerErrorException;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest request) throws InvalidDataException, ServerErrorException, ResourceNotFoundException;
    OrderResponse getOrderById(Long id) throws ResourceNotFoundException;
    ProductResponseDTO getProductFromProductService(Long productId) throws ServerErrorException;
}
