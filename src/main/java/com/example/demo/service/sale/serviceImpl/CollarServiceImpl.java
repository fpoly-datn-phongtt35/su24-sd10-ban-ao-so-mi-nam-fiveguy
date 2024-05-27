package com.example.demo.service.sale.serviceImpl;


import com.example.demo.entity.Collar;
import com.example.demo.repository.sale.CollarRepository;
import com.example.demo.service.sale.CollarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollarServiceImpl implements CollarService {

    @Autowired
    private CollarRepository collarRepository;

    @Override
    public List<Collar> getAllCollars() {
        return collarRepository.findAll();
    }
}
