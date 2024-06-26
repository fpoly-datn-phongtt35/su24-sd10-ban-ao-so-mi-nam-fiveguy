package com.example.demo.repository.sale;

import com.example.demo.entity.Collar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface SaleCollarRepository extends JpaRepository<Collar, Long> {
}
