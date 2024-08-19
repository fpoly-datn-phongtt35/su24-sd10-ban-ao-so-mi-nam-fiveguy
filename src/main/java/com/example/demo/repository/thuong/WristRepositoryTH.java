package com.example.demo.repository.thuong;

import com.example.demo.entity.Wrist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WristRepositoryTH extends JpaRepository<Wrist, Long> {
    @Query("SELECT w FROM Wrist w WHERE LOWER(w.name) LIKE LOWER(CONCAT('%', :name, '%')) AND (:status IS NULL OR w.status = :status)")
    Page<Wrist> findByNameContainingIgnoreCaseAndStatus(@Param("name") String name, @Param("status") Integer status, Pageable pageable);
    @Query("SELECT w FROM Wrist w WHERE :status IS NULL OR w.status = :status")
    Page<Wrist> findAllAndStatus(@Param("status") Integer status, Pageable pageable);
    List<Wrist> findAllByStatus(Integer status);
    Wrist findByName(String name);
}
