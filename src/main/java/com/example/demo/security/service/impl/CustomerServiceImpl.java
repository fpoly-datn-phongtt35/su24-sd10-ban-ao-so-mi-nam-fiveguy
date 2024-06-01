package com.example.demo.security.service.impl;

import com.example.demo.entity.Customer;
import com.example.demo.security.repository.CustomerRepository;
import com.example.demo.security.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer findByAccount_Id(Long accountId) {
        return customerRepository.findByAccount_Id(accountId);
    }

    @Override
    public Customer save(Customer customerEntity) {
        return customerRepository.save(customerEntity);
    }

    @Override
    public Customer createCustomer(Customer customerEntity) {
        Customer customer = new Customer();
        customer.setFullName(customerEntity.getFullName());
        customer.setAvatar(customerEntity.getAvatar());
        customer.setBirthDate(customerEntity.getBirthDate());
        customer.setGender(customerEntity.getGender());
        customer.setAccount(customerEntity.getAccount());
        customer.setCreatedAt(new Date());
        customer.setUpdatedAt(new Date());
        customer.setCreatedBy("Admin");
        customer.setUpdatedBy("Admin");
        customer.setStatus(1);
        return customerRepository.save(customer);
    }
}
