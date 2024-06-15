package com.example.demo.repository.nguyen;

import com.example.demo.entity.CustomerType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NCustomerTypeRepository extends JpaRepository<CustomerType, Long> {

    List<CustomerType> findAllByStatus(Integer status);
}
