package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.CustomerType;
import com.example.demo.repository.onlineShop.OLCustomerTypeRepository2;
import com.example.demo.service.onlineShop.OLCustomerTypeService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OLCustomerTypeServiceImpl2 implements OLCustomerTypeService2 {


    @Autowired
    private OLCustomerTypeRepository2 customerTypeRepository;

    @Override
    public Optional<CustomerType> getCustomerTypeByCode(Integer code) {
        return customerTypeRepository.findByCode(code);
    }
}
