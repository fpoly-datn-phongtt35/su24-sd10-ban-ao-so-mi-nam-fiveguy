package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.ProductDetail;
import com.example.demo.entity.Rating;
import com.example.demo.model.response.onlineShop.BillDetailResponse2;
import com.example.demo.repository.onlineShop.OLBillDetailRepository2;
import com.example.demo.service.onlineShop.OLBillDetailService2;
import com.example.demo.service.onlineShop.OLImageService2;
import com.example.demo.service.onlineShop.OlRatingService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class OLBillDetailServiceImpl2 implements OLBillDetailService2 {

    @Autowired
    private OLBillDetailRepository2 olBillDetailRepository;

    @Autowired
    private OLImageService2 olImageService2;

    @Autowired
    private OlRatingService2 olRatingService;

    @Override
    public List<BillDetail> findByProductDetail(ProductDetail productDetail) {

        return olBillDetailRepository.findByProductDetail(productDetail);

    }

    @Override
    public void saveAll(List<BillDetail> billDetails) {
        olBillDetailRepository.saveAll(billDetails);
    }

    @Override
    public List<BillDetail> getBillDetailsByBillId(Long billId) {
        return olBillDetailRepository.findByBillId(billId);
    }

    @Override
    public List<BillDetail> findAllByBillIdOrderByIdDesc(Long billId) {
        return olBillDetailRepository.findAllByBillIdOrderByIdDesc(billId);
    }

    @Override
    public List<BillDetailResponse2> gettBillDetailResponse2(Long id) {
        List<BillDetail> billDetails = getBillDetailsByBillId(id);
        List<BillDetailResponse2> responseList = new ArrayList<>();

        for (BillDetail billDetail : billDetails) {
            BillDetailResponse2 response = new BillDetailResponse2();
            response.setId(billDetail.getId());
            response.setQuantity(billDetail.getQuantity());
            response.setPrice(billDetail.getPrice());
            response.setPromotionalPrice(billDetail.getPromotionalPrice());
            response.setStatus(billDetail.getStatus());
            response.setBill(billDetail.getBill());
            response.setProductDetail(billDetail.getProductDetail());
            response.setDefectiveProduct(billDetail.getDefectiveProduct());
            response.setImagePath(olImageService2.findImagesByProductId(billDetail.getProductDetail().getProduct().getId()));

            List<Rating> list = olRatingService.findByBillDetail_Id(billDetail.getId());

            if (list.isEmpty() || (!list.get(0).isRated())) {
                response.setRated(false); // Thiết lập thuộc tính 'rated' dựa trên điều kiện
            } else {
                response.setRated(true);
            }


            responseList.add(response);
        }

        return responseList;
    }

    @Override
    public Integer getTotalQuantitySold(Long idProduct) {
        return olBillDetailRepository.getTotalQuantitySold(idProduct);
    }

}
