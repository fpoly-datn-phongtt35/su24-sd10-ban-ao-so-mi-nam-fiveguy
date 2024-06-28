package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NColorRepository extends JpaRepository<Color,Long> {

    List<Color> findAllByOrderByCreatedAtDesc();

    List<Color> findAllByStatusOrderByCreatedAtDesc(Integer status);
}