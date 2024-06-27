package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NMaterialRepository extends JpaRepository<Material,Long> {
}