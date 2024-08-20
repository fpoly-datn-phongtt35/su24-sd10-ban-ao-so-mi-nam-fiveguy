package com.example.demo.service.onlineShop;

import com.example.demo.entity.CustomerType;

import java.util.Optional;

public interface OLCustomerTypeService2 {
    Optional<CustomerType> getCustomerTypeByCode(Integer code);

}
