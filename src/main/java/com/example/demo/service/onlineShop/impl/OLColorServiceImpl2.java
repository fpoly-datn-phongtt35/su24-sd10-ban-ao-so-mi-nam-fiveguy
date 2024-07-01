package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.Color;
import com.example.demo.repository.onlineShop.OlColorRepository2;
import com.example.demo.repository.sale.ColorRepository2;
import com.example.demo.service.onlineShop.OLColorService2;
import com.example.demo.service.sale.ColorService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OLColorServiceImpl2 implements OLColorService2 {

    @Autowired
    private OlColorRepository2 colorRepository2;




    @Override
    public List<Object[]> getColorsByProductId(Long idProduct) {
        return colorRepository2.getColorsByProductId(idProduct);
    }
}