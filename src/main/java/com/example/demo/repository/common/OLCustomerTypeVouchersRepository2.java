package com.example.demo.repository.common;

import com.example.demo.entity.CustomerTypeVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OLCustomerTypeVouchersRepository2 extends JpaRepository<CustomerTypeVoucher, Long> {

    @Query("SELECT ctv.voucher.id FROM CustomerTypeVoucher ctv WHERE ctv.customerType.id = :customerTypeId")
    List<Long> findVoucherIdsByCustomerTypeId(@Param("customerTypeId") Long customerTypeId);


}
