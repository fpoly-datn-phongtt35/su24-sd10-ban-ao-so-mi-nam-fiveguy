package com.example.demo.service.common;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Voucher;

import java.util.List;

public interface OLVoucherService2 {

    List<Voucher> getAllVouchersByStatusAndApplyFor();

    List<Voucher> getVouchersForCustomer(Customer customer);

    Voucher selectBestVoucher(double totalAmount,Customer customer);
}
