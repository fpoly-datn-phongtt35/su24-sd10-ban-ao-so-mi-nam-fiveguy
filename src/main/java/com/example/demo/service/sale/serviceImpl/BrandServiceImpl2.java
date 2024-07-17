package com.example.demo.service.sale.serviceImpl;


import com.example.demo.entity.Brand;
import com.example.demo.repository.sale.BrandRepository2;
import com.example.demo.service.sale.BrandService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl2 implements BrandService2 {

    @Autowired
    private BrandRepository2 brandRepository2;

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository2.findByStatus(1);
    }
}