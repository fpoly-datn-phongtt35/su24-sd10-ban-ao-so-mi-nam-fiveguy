package com.example.demo.service.onlineShop.impl;

import com.example.demo.repository.onlineShop.OLSizeRepository2;
import com.example.demo.service.onlineShop.OLSizeService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OLSizeServiceImpl2 implements OLSizeService2 {

    @Autowired
    private OLSizeRepository2 sizeRepository2;


    @Override
    public List<Object[]> getSizesByProductId(Long idProduct) {
        return sizeRepository2.getSizesByProductId(idProduct);
    }
}