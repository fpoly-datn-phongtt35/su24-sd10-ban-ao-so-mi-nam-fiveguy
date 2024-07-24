package com.example.demo.repository.common;

import com.example.demo.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepositoryCommonRepository extends JpaRepository<Bill, Long> {

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.customer.id = :customerId AND b.voucher.id = :voucherId AND b.status NOT IN (5, 6)")
    Integer countVoucherUsageByCustomer(@Param("customerId") Long customerId, @Param("voucherId") Long voucherId);

}
