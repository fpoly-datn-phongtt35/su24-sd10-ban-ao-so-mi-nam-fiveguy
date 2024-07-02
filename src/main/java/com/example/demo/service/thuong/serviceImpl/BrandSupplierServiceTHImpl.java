package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.entity.BrandSuppiler;
import com.example.demo.repository.thuong.BrandSupplierRepositoryTH;
import com.example.demo.service.thuong.BrandSupplierServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandSupplierServiceTHImpl implements BrandSupplierServiceTH {
    @Autowired
    private BrandSupplierRepositoryTH repository;

    @Override
    public List<BrandSuppiler> findBySupplier_Id(Long id) {
        return repository.findBySupplier_Id(id);
    }
}
