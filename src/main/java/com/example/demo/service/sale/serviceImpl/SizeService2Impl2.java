package com.example.demo.service.sale.serviceImpl;

import com.example.demo.entity.Size;
import com.example.demo.repository.sale.SizeRepository2;
import com.example.demo.service.sale.SizeService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeService2Impl2 implements SizeService2 {

    @Autowired
    private SizeRepository2 sizeRepository2;

    @Override
    public List<Size> getAllSizes() {
        return sizeRepository2.findAll();
    }
}