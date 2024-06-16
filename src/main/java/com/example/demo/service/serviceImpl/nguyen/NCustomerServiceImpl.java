package com.example.demo.service.serviceImpl.nguyen;

import com.example.demo.entity.Customer;
import com.example.demo.repository.nguyen.NCustomerRepository;
import com.example.demo.service.nguyen.NCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NCustomerServiceImpl implements NCustomerService {

    @Autowired
    private NCustomerRepository customerRepository;

    public Page<Customer> getCustomers(String fullName, String phoneNumber,
                                       String email, Long customerTypeId, Pageable pageable) {
        return customerRepository
                .findCustomers(fullName, phoneNumber, email, customerTypeId, pageable);
    }
}
