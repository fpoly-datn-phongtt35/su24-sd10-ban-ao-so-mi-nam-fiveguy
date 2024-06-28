package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NSaleRepository extends JpaRepository<Sale,Long> {
}