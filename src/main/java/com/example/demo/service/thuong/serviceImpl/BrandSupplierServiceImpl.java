package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.entity.BrandSuppiler;
import com.example.demo.repository.thuong.BrandSupplierRepository;
import com.example.demo.service.thuong.BrandSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandSupplierServiceImpl implements BrandSupplierService {
    @Autowired
    private BrandSupplierRepository repository;

    @Override
    public List<BrandSuppiler> findBySupplier_Id(Long id) {
        return repository.findBySupplier_Id(id);
    }
}
