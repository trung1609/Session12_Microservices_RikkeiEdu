package com.trung.orderservice.controller;

import com.trung.orderservice.dto.OrderCreateRequest;
import com.trung.orderservice.dto.OrderResponse;
import com.trung.orderservice.dto.ProductResponseDTO;
import com.trung.orderservice.exception.InvalidDataException;
import com.trung.orderservice.exception.ResourceNotFoundException;
import com.trung.orderservice.exception.ServerErrorException;
import com.trung.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) throws InvalidDataException, ServerErrorException, ResourceNotFoundException {

        Thread thread1 = new Thread(() -> {
            try {
                orderService.createOrder(request);
                System.out.println("User 1");
            } catch (InvalidDataException e) {
                throw new RuntimeException(e);
            } catch (ServerErrorException e) {
                throw new RuntimeException(e);
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                orderService.createOrder(request);
            } catch (InvalidDataException e) {
                throw new RuntimeException(e);
            } catch (ServerErrorException e) {
                throw new RuntimeException(e);
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
            System.out.println("User 2");
        });
        thread1.start();
        thread2.start();

        return new ResponseEntity<>(orderService.createOrder(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }

    @GetMapping("/get-product/{productId}")
    public ResponseEntity<ProductResponseDTO> getProductFromProductService(@PathVariable Long productId) throws ServerErrorException {
        return new ResponseEntity<>(orderService.getProductFromProductService(productId), HttpStatus.OK);
    }
}
