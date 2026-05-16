package com.trung.customerservice.mapper;

import com.trung.customerservice.dto.CustomerRequestDTO;
import com.trung.customerservice.dto.CustomerResponseDTO;
import com.trung.customerservice.entity.Customer;

public class CustomerMapper {
    public static CustomerResponseDTO toDTO(Customer customer){
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .build();
    }
}
