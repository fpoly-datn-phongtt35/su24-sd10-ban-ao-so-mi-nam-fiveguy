package com.example.demo.repository.common;

import com.example.demo.entity.CustomerTypeVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerTypeVouchersCommonRepository extends JpaRepository<CustomerTypeVoucher, Long> {

    @Query("SELECT ctv FROM CustomerTypeVoucher ctv " +
            "WHERE ctv.customerType.id = :customerTypeId " +
            "AND ctv.customerType.status = 1")
    List<CustomerTypeVoucher> findByCustomerTypeId(@Param("customerTypeId") Long customerTypeId);


}
