package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NImageRepository extends JpaRepository<Image,Long> {
}