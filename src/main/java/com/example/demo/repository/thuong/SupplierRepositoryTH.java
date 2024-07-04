package com.example.demo.repository.thuong;

import com.example.demo.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepositoryTH extends JpaRepository<Supplier, Long> {
    @Query(value = "SELECT s from Supplier s WHERE s.name LIKE %:keyword% OR s.email LIKE %:keyword% OR s.phoneNumber LIKE %:keyword%")
    Page<Supplier> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    List<Supplier> findAllByStatus(Integer status);
    Supplier findByName(String name);
}
