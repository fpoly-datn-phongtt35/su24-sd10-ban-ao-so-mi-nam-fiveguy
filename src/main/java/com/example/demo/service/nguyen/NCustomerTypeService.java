package com.example.demo.service.nguyen;

import com.example.demo.entity.CustomerType;

import java.util.List;

public interface NCustomerTypeService {

    List<CustomerType> getAllCustomerType();

    CustomerType getCustomerTypeById(Long id);
}
