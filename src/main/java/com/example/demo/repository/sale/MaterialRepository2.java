package com.example.demo.repository.sale;

import com.example.demo.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface MaterialRepository2 extends JpaRepository<Material, Long> {
}
