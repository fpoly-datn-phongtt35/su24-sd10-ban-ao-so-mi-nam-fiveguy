package com.example.demo.security.repository;

import com.example.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface SCRoleRepository extends JpaRepository<Role, Long>{
        Optional<Role> findByFullNameAndStatus(String fullName,int status);
    }