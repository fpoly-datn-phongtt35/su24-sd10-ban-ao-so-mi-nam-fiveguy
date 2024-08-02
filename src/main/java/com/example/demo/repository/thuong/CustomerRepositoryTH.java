package com.example.demo.repository.thuong;

import com.example.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepositoryTH extends JpaRepository<Customer, Long> {

}
