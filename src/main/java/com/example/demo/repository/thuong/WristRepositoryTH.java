package com.example.demo.repository.thuong;

import com.example.demo.entity.Wrist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WristRepositoryTH extends JpaRepository<Wrist, Long> {
    Page<Wrist> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Wrist findByName(String name);
}
