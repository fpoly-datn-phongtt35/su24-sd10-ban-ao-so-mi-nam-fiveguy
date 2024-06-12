package com.example.demo.repository.nguyen;

import com.example.demo.entity.CustomerTypeVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTypeVoucherRepository extends JpaRepository<CustomerTypeVoucher, Long> {
}
