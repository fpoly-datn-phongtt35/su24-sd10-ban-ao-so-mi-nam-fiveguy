package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NCategoryRepository extends JpaRepository<Category,Long> {
}
