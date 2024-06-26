package com.example.demo.repository.sale;

import com.example.demo.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface SaleBrandRepository extends JpaRepository<Brand, Long> {
}