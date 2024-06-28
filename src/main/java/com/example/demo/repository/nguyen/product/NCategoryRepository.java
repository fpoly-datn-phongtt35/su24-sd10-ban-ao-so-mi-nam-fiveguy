package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NCategoryRepository extends JpaRepository<Category,Long> {

    List<Category> findAllByOrderByCreatedAtDesc();

    List<Category> findAllByStatusOrderByCreatedAtDesc(Integer status);
}
