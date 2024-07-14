package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.ProductDetail;
import com.example.demo.repository.onlineShop.OLBillDetailRepository2;
import com.example.demo.service.onlineShop.OLBillDetailService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class OLBillDetailServiceImpl2 implements OLBillDetailService2 {

    @Autowired
    private OLBillDetailRepository2 olBillDetailRepository;


    @Override
    public List<BillDetail> findByProductDetail(ProductDetail productDetail) {

        return olBillDetailRepository.findByProductDetail(productDetail);

    }

    @Override
    public void saveAll(List<BillDetail> billDetails) {
        olBillDetailRepository.saveAll(billDetails);
    }
}
