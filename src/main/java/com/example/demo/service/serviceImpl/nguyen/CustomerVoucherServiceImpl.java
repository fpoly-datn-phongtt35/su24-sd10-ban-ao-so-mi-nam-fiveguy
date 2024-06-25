package com.example.demo.service.serviceImpl.nguyen;

import com.example.demo.entity.Customer;
import com.example.demo.entity.CustomerVoucher;
import com.example.demo.repository.nguyen.CustomerVoucherRepository;
import com.example.demo.service.nguyen.CustomerVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerVoucherServiceImpl implements CustomerVoucherService {

    @Autowired
    CustomerVoucherRepository customerVoucherRepository;

    @Override
    public List<CustomerVoucher> getAll() {
        return customerVoucherRepository.findAll();
    }

    @Override
    public List<Customer> findCustomersByVoucherId(Long id) {
        return customerVoucherRepository.findCustomersByVoucherId(id);
    }

    @Override
    public CustomerVoucher getById(Long id) {
        return customerVoucherRepository.findById(id).get();
    }
}
