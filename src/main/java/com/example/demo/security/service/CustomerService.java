package com.example.demo.security.service;

import com.example.demo.entity.Customer;

import java.util.Optional;

public interface CustomerService {

    Optional<Customer> findById(Long Id);

    Customer findByAccount_Id(Long accountId);

    Customer save(Customer customerEntity);

    Customer createCustomer(Customer customerEntity);

}
