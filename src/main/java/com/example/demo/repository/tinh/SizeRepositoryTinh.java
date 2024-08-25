package com.example.demo.repository.tinh;

import com.example.demo.entity.Image;
import com.example.demo.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepositoryTinh extends JpaRepository<Size, Long> {
}
