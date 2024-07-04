package com.example.demo.repository.thuong;

import com.example.demo.entity.Wrist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WristRepositoryTH extends JpaRepository<Wrist, Long> {
    Page<Wrist> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Wrist> findAllByStatus(Integer status);
    Wrist findByName(String name);
}
