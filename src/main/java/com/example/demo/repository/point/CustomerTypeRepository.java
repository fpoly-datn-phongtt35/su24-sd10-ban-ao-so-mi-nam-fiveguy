package com.example.demo.repository.point;

import com.example.demo.entity.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerTypeRepository extends JpaRepository<CustomerType, Long>, JpaSpecificationExecutor<CustomerType> {
    boolean existsByCode(String code);
    List<CustomerType> findByStatus(Integer status);

    Optional<CustomerType> findByCustomersId(Long customerId);
    CustomerType findByCode(String customerId);


}