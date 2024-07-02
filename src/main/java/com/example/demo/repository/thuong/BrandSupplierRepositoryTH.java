package com.example.demo.repository.thuong;

import com.example.demo.entity.BrandSuppiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandSupplierRepositoryTH extends JpaRepository<BrandSuppiler, Long> {
    List<BrandSuppiler> findBySupplier_Id(Long id);
}
