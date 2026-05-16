package com.trung.productservice.controller;

import com.trung.productservice.dto.ProductRequestDTO;
import com.trung.productservice.dto.ProductResponseDTO;
import com.trung.productservice.event.OrderCreateEvent;
import com.trung.productservice.exception.ResourceNotFoundException;
import com.trung.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO){
        return new ResponseEntity<>(productService.createProduct(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

//    @PutMapping("/reduce-stock")
//    public ResponseEntity<?> reduceStock(@RequestBody OrderCreateEvent event) throws ResourceNotFoundException {
//        productService.reduceStock(event);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO requestDTO) throws ResourceNotFoundException {
        return new ResponseEntity<>(productService.updateProduct(id, requestDTO), HttpStatus.OK);
    }

    @PutMapping("/reduce-stock")
    public ResponseEntity<?> reduceStock(@RequestParam Long productId,
                                         @RequestParam Integer quantity) throws ResourceNotFoundException {
        productService.reduceStock(productId, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
