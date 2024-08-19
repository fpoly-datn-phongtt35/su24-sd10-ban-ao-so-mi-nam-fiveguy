package com.example.demo.repository.onlineShop;

import com.example.demo.entity.Color;
import com.example.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OLCustomerRepository2 extends JpaRepository<Customer, Long> {

}
