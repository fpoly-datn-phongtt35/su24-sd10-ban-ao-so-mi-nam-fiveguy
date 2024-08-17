package com.example.demo.service.thuong;

import com.example.demo.model.response.thuong.VoucherResponseTH;

import java.util.List;

public interface VoucherServiceTH {
    List<VoucherResponseTH> getVouchersForCustomer(Long id, String voucherName);
}
