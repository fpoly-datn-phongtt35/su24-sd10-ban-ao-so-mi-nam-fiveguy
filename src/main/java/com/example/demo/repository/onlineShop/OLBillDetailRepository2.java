package com.example.demo.repository.onlineShop;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.Color;
import com.example.demo.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OLBillDetailRepository2 extends JpaRepository<BillDetail, Long> {

    List<BillDetail> findByProductDetailAndStatus(ProductDetail productDetail, int status);



}
