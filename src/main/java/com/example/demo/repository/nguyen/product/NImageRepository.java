package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NImageRepository extends JpaRepository<Image,Long> {

    List<Image> findImagesByProductIdOrderByCreatedAtAsc(Long id);
}