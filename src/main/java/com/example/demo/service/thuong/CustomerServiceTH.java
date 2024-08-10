package com.example.demo.service.thuong;

import com.example.demo.entity.Customer;
import com.example.demo.model.response.thuong.CustomerResponseTH;

import java.util.List;

public interface CustomerServiceTH {
    CustomerResponseTH create(CustomerResponseTH customer, String name);
    List<Customer> searchCustomer(String keyword);
    CustomerResponseTH getOne(Long id);
}
