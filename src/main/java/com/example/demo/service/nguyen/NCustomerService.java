package com.example.demo.service.nguyen;

import com.example.demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NCustomerService {
    Page<Customer> getCustomers(String fullName, String phoneNumber,
                                String email, Long customerTypeId, Pageable pageable);
}
