package com.example.demo.repository.Customer;

import com.example.demo.entity.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CustomerTypeRepository extends JpaRepository<CustomerType, Long> {

}
