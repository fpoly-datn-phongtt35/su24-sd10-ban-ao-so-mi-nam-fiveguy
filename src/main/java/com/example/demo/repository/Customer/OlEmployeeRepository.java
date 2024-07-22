package com.example.demo.repository.Customer;

import com.example.demo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OlEmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByAccount_Id(Long accountId);
}
