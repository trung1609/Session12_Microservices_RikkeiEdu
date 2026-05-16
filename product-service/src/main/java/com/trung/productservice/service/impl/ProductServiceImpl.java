package com.trung.productservice.service.impl;

import com.trung.productservice.dto.ProductRequestDTO;
import com.trung.productservice.dto.ProductResponseDTO;
import com.trung.productservice.entity.Product;
import com.trung.productservice.event.OrderCreateEvent;
import com.trung.productservice.exception.ResourceNotFoundException;
import com.trung.productservice.mapper.ProductMapper;
import com.trung.productservice.repository.ProductRepository;
import com.trung.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        Product product = ProductMapper.toEntity(requestDTO);
        productRepository.save(product);

        return ProductMapper.toDTO(product);
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public ProductResponseDTO getProductById(Long id) throws ResourceNotFoundException {
        return productRepository.findById(id)
                .map(ProductMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toDTO)
                .toList();
    }

//    @Override
//    @KafkaListener(topics = "order-events", groupId = "reduce-stock")
//    public void reduceStock(OrderCreateEvent event) throws ResourceNotFoundException {
//        Product product = productRepository.findById(event.getProductId())
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + event.getProductId()));
//
//        if (product.getStockQuantity() < event.getQuantity()) {
//            throw new IllegalArgumentException("Insufficient stock for product id: " + event.getProductId());
//        }
//
//        product.setStockQuantity(product.getStockQuantity() - event.getQuantity());
//        log.info("Product stock reduced by {} for product id: {}", event.getQuantity(), event.getProductId());
//
//        productRepository.save(product);
//    }

    @Override
    public void reduceStock(Long productId, Integer quantity) throws ResourceNotFoundException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product id: " + productId);
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) throws ResourceNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setName(requestDTO.getName());
        product.setPrice(requestDTO.getPrice());
        product.setStockQuantity(requestDTO.getStockQuantity());
        productRepository.save(product);
        return ProductMapper.toDTO(product);
    }


}
