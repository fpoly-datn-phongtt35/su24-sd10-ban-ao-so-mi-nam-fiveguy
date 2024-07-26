package com.example.demo.service.nguyen;

import com.example.demo.entity.Customer;
import com.example.demo.entity.CustomerType;
import com.example.demo.entity.Voucher;
import com.example.demo.model.response.nguyen.CustomerVoucherStatsDTO;
import com.example.demo.model.response.nguyen.VoucherStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface NVoucherService {

    List<Voucher> getAllVoucher();

    Voucher getVoucherById(Long id);

    Voucher createVoucher(Voucher voucher);

    Voucher updateVoucher(Voucher voucher, Long id);

    Page<Voucher> findVouchers(String code, String name, Integer applyfor, Integer discountType,
                               Date startDate, Date endDate, Integer status, Pageable pageable);

    Voucher createVoucher(Voucher voucher, List<CustomerType> customerTypeList);

    Voucher updateVoucher(Long id, Voucher voucher, List<CustomerType> customerTypeList);

    VoucherStatistics calculateVoucherStatistics(Long voucherId);

    Page<CustomerVoucherStatsDTO> getCustomerVoucherStats(Long voucherId, Pageable pageable);

    List<Voucher> findAllVoucherCanUse(Long billId);
}
