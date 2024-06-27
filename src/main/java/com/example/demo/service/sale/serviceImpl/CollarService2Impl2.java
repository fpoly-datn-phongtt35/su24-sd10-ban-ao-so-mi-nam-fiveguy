package com.example.demo.service.sale.serviceImpl;


import com.example.demo.entity.Collar;
import com.example.demo.repository.sale.CollarRepository2;
import com.example.demo.service.sale.CollarService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollarService2Impl2 implements CollarService2 {

    @Autowired
    private CollarRepository2 collarRepository2;

    @Override
    public List<Collar> getAllCollars() {
        return collarRepository2.findAll();
    }
}
