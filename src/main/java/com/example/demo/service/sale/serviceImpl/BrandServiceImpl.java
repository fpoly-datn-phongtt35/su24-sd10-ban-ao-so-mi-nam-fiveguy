package com.example.demo.service.sale.serviceImpl;


import com.example.demo.entity.Brand;
import com.example.demo.repository.sale.BrandRepository;
import com.example.demo.service.sale.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }
}