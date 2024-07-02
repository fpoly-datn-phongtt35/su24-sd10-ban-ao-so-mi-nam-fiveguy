package com.example.demo.security.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface SCCustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByAccount_Id(Long accountId);
}
