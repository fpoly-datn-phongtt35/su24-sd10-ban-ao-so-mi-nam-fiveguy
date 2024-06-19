package com.example.demo.repository.nguyen;

import com.example.demo.entity.Customer;
import com.example.demo.entity.CustomerVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerVoucherRepository extends JpaRepository<CustomerVoucher, Long> {

    @Query("SELECT cv.customer FROM CustomerVoucher cv WHERE cv.voucher.id = :voucherId")
    List<Customer> findCustomersByVoucherId(@Param("voucherId") Long voucherId);
}
