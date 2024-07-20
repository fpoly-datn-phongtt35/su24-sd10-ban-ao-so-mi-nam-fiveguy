package com.example.demo.service.sale.serviceImpl;

import com.example.demo.entity.Color;
import com.example.demo.repository.sale.ColorRepository2;
import com.example.demo.service.sale.ColorService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServiceImpl2 implements ColorService2 {

    @Autowired
    private ColorRepository2 colorRepository2;

    @Override
    public List<Color> getAllColors() {
        return colorRepository2.findByStatus(1);
    }
}