package com.example.demo.repository.Customer;

import com.example.demo.entity.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OLCustomerTypeRepository extends JpaRepository<CustomerType, Long>, JpaSpecificationExecutor<CustomerType> {
    List<CustomerType> findByStatusOrderByMinPointsAscMaxPointsDesc(Integer status);
}