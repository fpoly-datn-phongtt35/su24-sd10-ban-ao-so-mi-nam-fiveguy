package com.example.demo.service;

import com.example.demo.entity.Voucher;

import java.util.List;

public interface VoucherService {

    List<Voucher> getAllVoucher();

    Voucher getVoucherById(Long id);

    Voucher createVoucher(Voucher voucher);

    Voucher updateVoucher(Voucher voucher, Long id);

}
