package com.trung.orderservice.service.impl;

import com.trung.orderservice.dto.OrderCreateRequest;
import com.trung.orderservice.dto.OrderResponse;
import com.trung.orderservice.dto.ProductResponseDTO;
import com.trung.orderservice.entity.Orders;
import com.trung.orderservice.exception.InvalidDataException;
import com.trung.orderservice.exception.ResourceNotFoundException;
import com.trung.orderservice.exception.ServerErrorException;
import com.trung.orderservice.mapper.OrderMapper;
import com.trung.orderservice.repository.OrderRepository;
import com.trung.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;
    private final CustomerClient customerClient;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) throws InvalidDataException, ServerErrorException, ResourceNotFoundException {
            ProductResponseDTO product = productClient.getProductById(request.getProductId());
            if (customerClient.getCustomerById(request.getCustomerId()).getBody() == null) {
                throw new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId());
            }
            if (product.getStockQuantity() < request.getQuantity()) {
                throw new InvalidDataException("Insufficient stock for product id: " + request.getProductId());
            }

            productClient.reduceStock(request.getProductId(), request.getQuantity());

            Orders orders = new Orders();
            orders.setCustomerId(request.getCustomerId());
            orders.setProductId(request.getProductId());
            orders.setTotalAmount(product.getPrice() * request.getQuantity());

            orderRepository.save(orders);
            return OrderMapper.toDTO(orders);
    }

    @Override
    public OrderResponse getOrderById(Long id) throws ResourceNotFoundException {
        return orderRepository.findById(id)
                .map(OrderMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Override
    public ProductResponseDTO getProductFromProductService(Long productId) throws ServerErrorException {
        List<ServiceInstance> instances = discoveryClient.getInstances("PRODUCT-SERVICE");
        if (instances.isEmpty()) {
            throw new ServerErrorException("No instances of PRODUCT-SERVICE found");
        }

        ServiceInstance instance = instances.get(0);

        String targetUrl = "http://PRODUCT-SERVICE/api/v1/products/" + productId;
        return restTemplate.getForObject(targetUrl, ProductResponseDTO.class);
    }
}
