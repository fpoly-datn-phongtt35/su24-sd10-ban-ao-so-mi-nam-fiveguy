package com.example.demo.repository.nguyen;

import com.example.demo.entity.CustomerTypeVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NCustomerTypeVoucherRepository extends JpaRepository<CustomerTypeVoucher, Long> {

    List<CustomerTypeVoucher> findAllByVoucherId(Long id);
}
