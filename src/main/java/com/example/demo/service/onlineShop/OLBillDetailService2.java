package com.example.demo.service.onlineShop;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.ProductDetail;

import java.util.List;

public interface OLBillDetailService2 {

    List<BillDetail> findByProductDetail(ProductDetail productDetail);


}
