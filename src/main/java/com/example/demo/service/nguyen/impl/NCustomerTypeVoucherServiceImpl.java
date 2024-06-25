package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.CustomerTypeVoucher;
import com.example.demo.repository.nguyen.NCustomerTypeVoucherRepository;
import com.example.demo.service.nguyen.NCustomerTypeVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NCustomerTypeVoucherServiceImpl implements NCustomerTypeVoucherService {

    @Autowired
    NCustomerTypeVoucherRepository NCustomerTypeVoucherRepository;

    @Override
    public List<CustomerTypeVoucher> getAllCusomerTypeVouchers() {
        return NCustomerTypeVoucherRepository.findAll();
    }

    @Override
    public CustomerTypeVoucher getCustomerTypeVoucherById(Long id) {
        return NCustomerTypeVoucherRepository.findById(id).get();
    }

    @Override
    public List<CustomerTypeVoucher> getAllByVoucherId(Long id) {
        return NCustomerTypeVoucherRepository.findAllByVoucherId(id);
    }
}
