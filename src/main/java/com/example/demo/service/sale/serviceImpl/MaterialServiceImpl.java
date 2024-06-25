package com.example.demo.service.sale.serviceImpl;


import com.example.demo.entity.Material;
import com.example.demo.repository.sale.MaterialRepository;
import com.example.demo.service.sale.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Override
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }
}