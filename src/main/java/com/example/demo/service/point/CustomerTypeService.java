package com.example.demo.service.point;

import com.example.demo.entity.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerTypeService {
    CustomerType saveCustomerType(CustomerType customerType);
    CustomerType updateCustomerType(CustomerType customerType);
    Page<CustomerType> getCustomerTypes(String name, Pageable pageable);
    CustomerType getCustomerTypeById(Long id);
}