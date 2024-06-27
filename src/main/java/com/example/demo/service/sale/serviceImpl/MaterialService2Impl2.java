package com.example.demo.service.sale.serviceImpl;


import com.example.demo.entity.Material;
import com.example.demo.repository.sale.MaterialRepository2;
import com.example.demo.service.sale.MaterialService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService2Impl2 implements MaterialService2 {

    @Autowired
    private MaterialRepository2 materialRepository2;

    @Override
    public List<Material> getAllMaterials() {
        return materialRepository2.findAll();
    }
}