package com.example.demo.service.sale.serviceImpl;

import com.example.demo.entity.Wrist;
import com.example.demo.repository.sale.WristRepository;
import com.example.demo.service.sale.WristService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WristServiceImpl implements WristService {

    @Autowired
    private WristRepository wristRepository;

    @Override
    public List<Wrist> getAllWrists() {
        return wristRepository.findAll();
    }
}