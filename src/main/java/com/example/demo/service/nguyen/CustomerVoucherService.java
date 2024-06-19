package com.example.demo.service.nguyen;

import com.example.demo.entity.Customer;
import com.example.demo.entity.CustomerVoucher;

import java.util.List;

public interface CustomerVoucherService {

    List<CustomerVoucher> getAll();

    List<Customer> findCustomersByVoucherId(Long id);

    CustomerVoucher getById(Long id);
}
