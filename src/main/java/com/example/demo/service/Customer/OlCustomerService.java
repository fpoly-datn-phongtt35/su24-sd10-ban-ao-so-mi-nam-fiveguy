package com.example.demo.service.Customer;


import com.example.demo.entity.Customer;

import java.util.Optional;

public interface OlCustomerService {

    Optional<Customer> findById(Long Id);

    Customer findByAccount_Id(Long accountId);

    Customer save(Customer customerEntity);



}
