package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NImageRepository extends JpaRepository<Image,Long> {
}