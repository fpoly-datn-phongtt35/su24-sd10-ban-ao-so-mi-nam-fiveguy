package com.example.demo.security.service;

import com.example.demo.entity.Customer;

import java.util.Optional;

public interface CustomerService {

    Customer findByAccount_Id(Long accountId);

    Customer save(Customer customerEntity);

    Customer createCustomer(Customer customerEntity);

    Optional<Customer> getCustomerByToken(String token);

}
