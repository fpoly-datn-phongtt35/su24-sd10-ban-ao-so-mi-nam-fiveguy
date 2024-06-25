package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.Customer;
import com.example.demo.entity.CustomerVoucher;
import com.example.demo.repository.nguyen.NCustomerVoucherRepository;
import com.example.demo.service.nguyen.NCustomerVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NCustomerVoucherServiceImpl implements NCustomerVoucherService {

    @Autowired
    NCustomerVoucherRepository NCustomerVoucherRepository;

    @Override
    public List<CustomerVoucher> getAll() {
        return NCustomerVoucherRepository.findAll();
    }

    @Override
    public List<Customer> findCustomersByVoucherId(Long id) {
        return NCustomerVoucherRepository.findCustomersByVoucherId(id);
    }

    @Override
    public CustomerVoucher getById(Long id) {
        return NCustomerVoucherRepository.findById(id).get();
    }
}
