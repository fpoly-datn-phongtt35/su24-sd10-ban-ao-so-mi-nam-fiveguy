package com.example.demo.repository.Customer;

import com.example.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleCustomerRepositoryH extends JpaRepository<Role, Long> {
    Role findByFullName(String fullName);
}
