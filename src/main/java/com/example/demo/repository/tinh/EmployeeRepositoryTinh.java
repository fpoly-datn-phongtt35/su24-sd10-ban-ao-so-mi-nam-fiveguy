package com.example.demo.repository.tinh;
import com.example.demo.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepositoryTinh extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    // gen Code Tăng dần
    @Query("SELECT code FROM Employee code")
    List<Employee> genCode();

    // Tim theo ma
    @Query("SELECT m FROM Employee m WHERE m.code LIKE %:code%")
    Page<Employee> searchMa(String code,Pageable pageable);

    //Employee status = 1
    @Query("SELECT m FROM Employee m WHERE m.status = 1")
    List<Employee> getAllStatusDangLam();

    @Query("SELECT m FROM Employee m WHERE m.status = :status")
    List<Employee> getAllStatus(Integer status);

    @Modifying
    @Query("update Employee m set m.status = 2 where m.id=:id")
    void updateStatusEmployee(Long id);


    Optional<Employee> findByAccount_Id(Long accountId);
}
