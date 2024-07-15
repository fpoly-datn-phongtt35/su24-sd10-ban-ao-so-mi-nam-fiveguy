package com.example.demo.service.thuong;

import com.example.demo.entity.BrandSuppiler;

import java.util.List;

public interface BrandSupplierServiceTH {
    List<BrandSuppiler> findBySupplier_Id(Long id);
}
