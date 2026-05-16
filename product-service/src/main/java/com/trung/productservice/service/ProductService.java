package com.trung.productservice.service;

import com.trung.productservice.dto.ProductRequestDTO;
import com.trung.productservice.dto.ProductResponseDTO;
import com.trung.productservice.event.OrderCreateEvent;
import com.trung.productservice.exception.ResourceNotFoundException;

import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO requestDTO);
    ProductResponseDTO getProductById(Long id) throws ResourceNotFoundException;
    List<ProductResponseDTO> getAllProducts();
//    void reduceStock(OrderCreateEvent event) throws ResourceNotFoundException;
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) throws ResourceNotFoundException;
    void reduceStock(Long productId, Integer quantity) throws ResourceNotFoundException;
}
