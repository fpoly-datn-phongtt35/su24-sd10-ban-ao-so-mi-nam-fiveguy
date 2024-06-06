package com.example.demo.service.nguyen;

import com.example.demo.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface VoucherService {

    List<Voucher> getAllVoucher();

    Voucher getVoucherById(Long id);

    Voucher createVoucher(Voucher voucher);

    Voucher updateVoucher(Voucher voucher, Long id);

    Page<Voucher> findVouchers(String name, String code, Integer discountType, Date startDate, Date endDate, Integer status, Pageable pageable);

}
