package com.trung.customerservice.controller;

import com.trung.customerservice.dto.CustomerLoginRequest;
import com.trung.customerservice.dto.CustomerRequestDTO;
import com.trung.customerservice.dto.CustomerResponseDTO;
import com.trung.customerservice.exception.InvalidCredentialException;
import com.trung.customerservice.exception.ResourceConflictException;
import com.trung.customerservice.exception.ResourceNotFoundException;
import com.trung.customerservice.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO requestDTO) throws ResourceConflictException, ResourceNotFoundException {
        return new ResponseEntity<>(customerService.createCustomer(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(customerService.getCustomerById(id), HttpStatus.OK);
    }

    @PutMapping("/login")
    public ResponseEntity<CustomerResponseDTO> loginCustomer(@Valid @RequestBody CustomerLoginRequest requestDTO) throws ResourceNotFoundException, ResourceConflictException, InvalidCredentialException {
        return new ResponseEntity<>(customerService.loginCustomer(requestDTO), HttpStatus.OK);
    }
}
