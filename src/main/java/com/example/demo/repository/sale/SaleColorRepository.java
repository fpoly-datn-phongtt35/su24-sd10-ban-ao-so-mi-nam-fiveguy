package com.example.demo.repository.sale;

import com.example.demo.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface SaleColorRepository extends JpaRepository<Color, Long> {
}
