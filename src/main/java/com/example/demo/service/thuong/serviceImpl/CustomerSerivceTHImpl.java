package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.entity.Customer;
import com.example.demo.model.response.thuong.CustomerResponseTH;
import com.example.demo.repository.thuong.CustomerRepositoryTH;
import com.example.demo.service.thuong.CustomerServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public CustomerResponseTH create(Customer customerRequest) {
        customerRequest.setStatus(1);
        return setBillResponse(customerRepository.save(customerRequest));
    }
}
