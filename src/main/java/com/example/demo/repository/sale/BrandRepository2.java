package com.example.demo.repository.sale;

import com.example.demo.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface BrandRepository2 extends JpaRepository<Brand, Long> {

    List<Brand> findByStatus(int status);
}