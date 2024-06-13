package com.example.demo.service.serviceImpl.nguyen;

import com.example.demo.entity.CustomerTypeVoucher;
import com.example.demo.repository.nguyen.CustomerTypeVoucherRepository;
import com.example.demo.service.nguyen.CustomerTypeVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerTypeVoucherServiceImpl implements CustomerTypeVoucherService {

    @Autowired
    CustomerTypeVoucherRepository customerTypeVoucherRepository;

    @Override
    public List<CustomerTypeVoucher> getAllCusomerTypeVouchers() {
        return customerTypeVoucherRepository.findAll();
    }

    @Override
    public CustomerTypeVoucher getCustomerTypeVoucherById(Long id) {
        return customerTypeVoucherRepository.findById(id).get();
    }

    @Override
    public List<CustomerTypeVoucher> getAllByVoucherId(Long id) {
        return customerTypeVoucherRepository.findAllByVoucherId(id);
    }
}
