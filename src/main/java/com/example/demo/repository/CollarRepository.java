package com.example.demo.repository;

import com.example.demo.entity.Collar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollarRepository extends JpaRepository<Collar, Long> {
    Page<Collar> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Collar findByName(String name);
}
