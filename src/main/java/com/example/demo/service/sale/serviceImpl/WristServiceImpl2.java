package com.example.demo.service.sale.serviceImpl;

import com.example.demo.entity.Wrist;
import com.example.demo.repository.sale.WristRepository2;
import com.example.demo.service.sale.WristService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WristServiceImpl2 implements WristService2 {

    @Autowired
    private WristRepository2 wristRepository2;

    @Override
    public List<Wrist> getAllWrists() {
        return wristRepository2.findAll();
    }
}