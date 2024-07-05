package com.example.demo.service.tinh.serviceImpl;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import com.example.demo.repository.tinh.ProductDetaillRepositoryTinh;
import com.example.demo.service.tinh.ProductDetailServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDetailServiceImplTinh implements ProductDetailServiceTinh {
    @Autowired
    ProductDetaillRepositoryTinh productRepositoryTinh;

    @Override
    public List<ProductDetail> getAll(){
        return productRepositoryTinh.findAll();
    }
}
