package com.trung.customerservice.service;

import com.trung.customerservice.dto.CustomerLoginRequest;
import com.trung.customerservice.dto.CustomerRequestDTO;
import com.trung.customerservice.dto.CustomerResponseDTO;
import com.trung.customerservice.exception.InvalidCredentialException;
import com.trung.customerservice.exception.ResourceConflictException;
import com.trung.customerservice.exception.ResourceNotFoundException;

public interface CustomerService {
    CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) throws ResourceNotFoundException, ResourceConflictException;
    CustomerResponseDTO getCustomerById(Long id) throws ResourceNotFoundException;
    CustomerResponseDTO loginCustomer(CustomerLoginRequest request) throws ResourceNotFoundException, InvalidCredentialException;
}
