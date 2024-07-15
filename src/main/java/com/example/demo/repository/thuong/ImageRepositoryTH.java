package com.example.demo.repository.thuong;

import com.example.demo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepositoryTH extends JpaRepository<Image, Long> {
    List<Image> findAllByProduct_Id(Long id);
}
