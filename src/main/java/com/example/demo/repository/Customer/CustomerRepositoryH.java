package com.example.demo.repository.Customer;

import com.example.demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepositoryH extends JpaRepository<Customer, Long> {
    // gen Code Tăng dần
    @Query("SELECT code FROM Customer code")
    List<Customer> genCode();
    // Tim theo ma
    @Query("SELECT m FROM Customer m WHERE m.code LIKE %:code%")
    Page<Customer> searchMa(String code, Pageable pageable);

    //Customer status = 1
    @Query("SELECT m FROM Customer m WHERE m.status = 1")
    List<Customer> getAllStatusDangLam();

    @Query("SELECT m FROM Customer m WHERE m.status = :status")
    List<Customer> getAllStatus(Integer status);

    @Modifying
    @Query("update Customer m set m.status = 2 where m.id=:id")
    void updateStatusCustomer(Long id);


    Optional<Customer> findByAccount_Id(Long accountId);
}
