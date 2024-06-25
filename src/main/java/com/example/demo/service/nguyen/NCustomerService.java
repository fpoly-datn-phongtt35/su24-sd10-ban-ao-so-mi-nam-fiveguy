package com.example.demo.service.nguyen;

import com.example.demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NCustomerService {

    List<Customer> getAll();

    Page<Customer> getCustomers(String fullName, String phoneNumber,
                                String email, Long customerTypeId, Pageable pageable);
}
