package com.example.demo.service.sale.serviceImpl;

import com.example.demo.entity.Size;
import com.example.demo.repository.sale.SizeRepository;
import com.example.demo.service.sale.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }
}