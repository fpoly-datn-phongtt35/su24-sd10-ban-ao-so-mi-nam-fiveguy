package com.example.demo.service.nguyen;

import com.example.demo.entity.CustomerType;
import com.example.demo.entity.CustomerTypeVoucher;

import java.util.List;

public interface CustomerTypeVoucherService {

    List<CustomerTypeVoucher> getAllCusomerTypeVouchers();

    CustomerTypeVoucher getCustomerTypeVoucherById(Long id);

    List<CustomerTypeVoucher> getAllByVoucherId(Long id);
}
