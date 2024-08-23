package com.example.demo.repository.thuong;

import com.example.demo.entity.CustomerTypeVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerTypeVouchersRepositoryTH extends JpaRepository<CustomerTypeVoucher, Long> {
    @Query("SELECT ctv.voucher.id FROM CustomerTypeVoucher ctv WHERE ctv.customerType.id = :customerTypeId")
    List<Long> findVoucherIdsByCustomerTypeId(@Param("customerTypeId") Long customerTypeId);

    @Query("SELECT ctv FROM CustomerTypeVoucher ctv " +
            "WHERE ctv.customerType.id = :customerTypeId " +
            "AND ctv.customerType.status = 1")
    List<CustomerTypeVoucher> findByCustomerTypeId(@Param("customerTypeId") Long customerTypeId);
}
