package com.example.demo.service.tinh.serviceImpl;

import com.example.demo.entity.ProductDetail;
import com.example.demo.repository.tinh.ProductDetailSpecificationTinh;
import com.example.demo.repository.tinh.ProductDetaillRepositoryTinh;
import com.example.demo.service.tinh.ProductDetailServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductDetailServiceImplTinh implements ProductDetailServiceTinh {
    @Autowired
    ProductDetaillRepositoryTinh productRepositoryTinh;

    @Autowired
    ProductDetaillRepositoryTinh productDetaillRepositoryTinh;

    @Override
    public List<ProductDetail> getAll(){
        return productRepositoryTinh.findAll();
    }

    @Override
    public Page<ProductDetail> findProductDetal(String name, String code, BigDecimal price, Pageable pageable) {

        Specification<ProductDetail> spec = Specification.where(ProductDetailSpecificationTinh.hasCode(code))
                .and(ProductDetailSpecificationTinh.hasName(name))
                .and(ProductDetailSpecificationTinh.hasPrice(price));


        return productDetaillRepositoryTinh.findAll(spec, pageable);
    }
}
