package com.example.demo.repository.thuong;

import com.example.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepositoryTH extends JpaRepository<Customer, Long> {

    @Query(value = "SELECT c FROM Customer c WHERE c.fullName LIKE %:keyword% OR c.code LIKE %:keyword% AND c.status = 1")
    List<Customer> findAllByStatus(@Param("keyword") String keyword);
}
