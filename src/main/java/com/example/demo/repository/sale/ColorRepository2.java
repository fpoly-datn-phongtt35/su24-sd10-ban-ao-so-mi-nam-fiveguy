package com.example.demo.repository.sale;

import com.example.demo.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ColorRepository2 extends JpaRepository<Color, Long> {

    List<Color> findByStatus(int status);
}
