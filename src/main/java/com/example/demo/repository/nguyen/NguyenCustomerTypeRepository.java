package com.example.demo.repository.nguyen;

import com.example.demo.entity.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NguyenCustomerTypeRepository extends JpaRepository<CustomerType, Long> {
}
