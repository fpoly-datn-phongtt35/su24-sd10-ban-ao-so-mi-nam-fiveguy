package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.entity.ProductDetail;
import com.example.demo.repository.thuong.ProductDetailRepositoryTH;
import com.example.demo.service.thuong.ProductDetailServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDetailServiceTHImpl implements ProductDetailServiceTH {
    @Autowired
    private ProductDetailRepositoryTH repository;

    @Override
    public List<ProductDetail> findAllByProduct_Id(Long id) {
        return repository.findAllByProduct_Id(id);
    }
}
