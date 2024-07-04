package com.example.demo.repository.thuong;

import com.example.demo.entity.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepositoryTH extends JpaRepository<Color, Long> {
    @Query(value = "SELECT c from Color c WHERE c.name LIKE %:keyword% OR c.colorCode LIKE %:keyword%")
    Page<Color> findByNameOrColorCode(@Param("keyword") String keyword, Pageable pageable);
    Color findByName(String name);
    Color findByColorCode(String colorCode);
}
