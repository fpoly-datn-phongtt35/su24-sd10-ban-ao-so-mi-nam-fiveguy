package com.example.demo.repository.common;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CustomerCommonRepository extends JpaRepository<Customer, Long> {


}
