package com.example.demo.repository.common;

import com.example.demo.entity.CustomerVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OLCustomerVoucherRepository2 extends JpaRepository<CustomerVoucher, Long> {

    @Query("SELECT cv.voucher.id FROM CustomerVoucher cv WHERE cv.customer.id = :customerId")
    List<Long> findVoucherIdsByCustomerId(@Param("customerId") Long customerId);

}
