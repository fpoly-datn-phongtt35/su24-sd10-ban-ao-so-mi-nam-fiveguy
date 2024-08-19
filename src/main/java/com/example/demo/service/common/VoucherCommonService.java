package com.example.demo.service.common;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Voucher;
import com.example.demo.model.response.common.VoucherDTO;

import java.util.List;

public interface VoucherCommonService {

    List<Voucher> getAllVouchersByStatusAndApplyFor();

    List<VoucherDTO> getVouchersForCustomer(Customer customer, String voucherName);

    List<VoucherDTO> getVouchersForCustomer(Long id, String voucherName);


//    Voucher selectBestVoucher(double totalAmount,Customer customer);
}
