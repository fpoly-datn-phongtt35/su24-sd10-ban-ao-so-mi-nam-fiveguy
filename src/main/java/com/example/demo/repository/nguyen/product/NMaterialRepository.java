package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NMaterialRepository extends JpaRepository<Material,Long> {

    List<Material> findAllByOrderByCreatedAtDesc();

    List<Material> findAllByStatusOrderByCreatedAtDesc(Integer status);
}