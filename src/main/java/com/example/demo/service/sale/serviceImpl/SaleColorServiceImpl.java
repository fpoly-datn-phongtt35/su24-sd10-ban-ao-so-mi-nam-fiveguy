package com.example.demo.service.sale.serviceImpl;

import com.example.demo.entity.Color;
import com.example.demo.repository.sale.SaleColorRepository;
import com.example.demo.service.sale.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleColorServiceImpl implements ColorService {

    @Autowired
    private SaleColorRepository saleColorRepository;

    @Override
    public List<Color> getAllColors() {
        return saleColorRepository.findAll();
    }
}