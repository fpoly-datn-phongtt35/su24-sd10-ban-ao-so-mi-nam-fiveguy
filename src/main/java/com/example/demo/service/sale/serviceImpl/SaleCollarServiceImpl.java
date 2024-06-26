package com.example.demo.service.sale.serviceImpl;


import com.example.demo.entity.Collar;
import com.example.demo.repository.sale.SaleCollarRepository;
import com.example.demo.service.sale.CollarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleCollarServiceImpl implements CollarService {

    @Autowired
    private SaleCollarRepository saleCollarRepository;

    @Override
    public List<Collar> getAllCollars() {
        return saleCollarRepository.findAll();
    }
}
