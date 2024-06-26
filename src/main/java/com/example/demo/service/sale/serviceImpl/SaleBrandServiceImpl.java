package com.example.demo.service.sale.serviceImpl;


import com.example.demo.entity.Brand;
import com.example.demo.repository.sale.SaleBrandRepository;
import com.example.demo.service.sale.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleBrandServiceImpl implements BrandService {

    @Autowired
    private SaleBrandRepository saleBrandRepository;

    @Override
    public List<Brand> getAllBrands() {
        return saleBrandRepository.findAll();
    }
}