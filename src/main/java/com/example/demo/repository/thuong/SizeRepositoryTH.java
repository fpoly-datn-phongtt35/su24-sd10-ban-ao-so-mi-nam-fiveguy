package com.example.demo.repository.thuong;

import com.example.demo.entity.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepositoryTH extends JpaRepository<Size, Long> {
    @Query("SELECT s FROM Size s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) AND (:status IS NULL OR s.status = :status)")
    Page<Size> findByNameContainingIgnoreCaseAndStatus(@Param("name") String name, @Param("status") Integer status, Pageable pageable);
    @Query("SELECT s FROM Size s WHERE :status IS NULL OR s.status = :status")
    Page<Size> findAllAndStatus(@Param("status") Integer status, Pageable pageable);
    List<Size> findAllByStatus(Integer status);
    Size findByName(String name);
}
