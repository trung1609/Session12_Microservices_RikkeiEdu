package com.trung.customerservice.service.impl;

import com.trung.customerservice.dto.CustomerLoginRequest;
import com.trung.customerservice.dto.CustomerRequestDTO;
import com.trung.customerservice.dto.CustomerResponseDTO;
import com.trung.customerservice.entity.Customer;
import com.trung.customerservice.exception.InvalidCredentialException;
import com.trung.customerservice.exception.ResourceConflictException;
import com.trung.customerservice.exception.ResourceNotFoundException;
import com.trung.customerservice.mapper.CustomerMapper;
import com.trung.customerservice.repository.CustomerRepository;
import com.trung.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) throws ResourceConflictException {
        if (customerRepository.existsByEmail(requestDTO.getEmail())) {
            throw new ResourceConflictException("Email already exists");
        }

        Customer customer = new Customer();
        customer.setEmail(requestDTO.getEmail());
        customer.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        customer.setAddress(requestDTO.getAddress());
        customer.setFullName(requestDTO.getFullName());
        customerRepository.save(customer);
        return CustomerMapper.toDTO(customer);
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) throws ResourceNotFoundException {
        return customerRepository.findById(id)
                .map(CustomerMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    @Override
    public CustomerResponseDTO loginCustomer(CustomerLoginRequest request) throws ResourceNotFoundException, InvalidCredentialException {
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialException("Invalid email or password"));
        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new InvalidCredentialException("Invalid email or password");
        }

        return CustomerMapper.toDTO(customer);
    }
}
