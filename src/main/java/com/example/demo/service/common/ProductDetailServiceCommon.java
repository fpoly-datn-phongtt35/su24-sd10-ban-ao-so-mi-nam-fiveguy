package com.example.demo.service.common;

import com.example.demo.entity.ProductDetail;
import com.example.demo.model.response.common.ProductDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductDetailServiceCommon {


    Page<ProductDetailDTO> getFilteredProductDetails(List<Long> colorIds, List<Long> sizeIds, List<Long> materialIds,
                                                     List<Long> collarIds, List<Long> wristIds, List<Long> categoryIds,
                                                     String search, Pageable pageable);
}
