package com.example.demo.service.onlineShop;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.ProductDetail;
import com.example.demo.model.response.onlineShop.BillDetailResponse2;

import java.util.List;

public interface OLBillDetailService2 {

    List<BillDetail> findByProductDetail(ProductDetail productDetail);

    void saveAll(List<BillDetail> billDetails);

    List<BillDetail> getBillDetailsByBillId(Long billId);

    List<BillDetail> findAllByBillIdOrderByIdDesc(Long billId);

    public List<BillDetailResponse2> gettBillDetailResponse2(Long id);
}
