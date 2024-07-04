package com.example.demo.repository.thuong;

import com.example.demo.entity.Collar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollarRepositoryTH extends JpaRepository<Collar, Long> {
    Page<Collar> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Collar> findAllByStatus(Integer status);
    Collar findByName(String name);
}
