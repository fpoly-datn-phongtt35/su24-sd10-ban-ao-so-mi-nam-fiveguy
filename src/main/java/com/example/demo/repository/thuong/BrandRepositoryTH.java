package com.example.demo.repository.thuong;

import com.example.demo.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BrandRepositoryTH extends JpaRepository<Brand, Long> {
    @Query("SELECT b FROM Brand b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%')) AND (:status IS NULL OR b.status = :status)")
    Page<Brand> findByNameContainingIgnoreCaseAndStatus(String name, Integer status, Pageable pageable);
    @Query("SELECT b FROM Brand b WHERE :status IS NULL OR b.status = :status")
    Page<Brand> findAllAndStatus(@Param("status") Integer status, Pageable pageable);
    Brand findByName(String name);

}
