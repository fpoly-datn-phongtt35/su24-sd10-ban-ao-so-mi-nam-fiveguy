package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.entity.Customer;
import com.example.demo.model.response.thuong.CustomerResponseTH;
import com.example.demo.repository.thuong.CustomerRepositoryTH;
import com.example.demo.service.thuong.CustomerServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
public class CustomerSerivceTHImpl implements CustomerServiceTH {

    @Autowired
    private CustomerRepositoryTH customerRepository;

    public CustomerResponseTH setBillResponse(Customer customer) {
        CustomerResponseTH customerResponse = new CustomerResponseTH();
        customerResponse.setId(customer.getId());
        customerResponse.setCode(customer.getCode());
        customerResponse.setFullName(customer.getFullName());
        customerResponse.setAvatar(customer.getAvatar());
        customerResponse.setBirthDate(customer.getBirthDate());
        customerResponse.setGender(customer.getGender());
        customerResponse.setCreatedAt(customer.getCreatedAt());
        customerResponse.setUpdatedAt(customer.getUpdatedAt());
        customerResponse.setCreatedBy(customer.getCreatedBy());
        customerResponse.setUpdatedBy(customer.getUpdatedBy());
        customerResponse.setStatus(customer.getStatus());
        customerResponse.setBills(customer.getBills());
        customerResponse.setAddresses(customer.getAddresses());
        return customerResponse;
    }

    @Override
    public CustomerResponseTH create(CustomerResponseTH customerRequest, String name) {
        Customer customer = new Customer();
        customer.setCode("KH" + Integer.parseInt(Long.toString(System.currentTimeMillis()).substring(7)));
        customer.setFullName(customerRequest.getFullName());
        customer.setAvatar(customerRequest.getAvatar());
        customer.setBirthDate(customerRequest.getBirthDate());
        customer.setGender(customerRequest.getGender());
        customer.setCreatedAt(new Date());
        customer.setCreatedBy(name);
        customer.setStatus(1);
        customerRequest.getAddresses().forEach(d -> d.setCustomer(customer));
        customer.setAddresses(customerRequest.getAddresses());
        return setBillResponse(customerRepository.save(customer));
    }
}
