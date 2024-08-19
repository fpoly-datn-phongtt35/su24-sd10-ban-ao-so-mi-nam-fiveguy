package com.example.demo.service.common.impl;

import com.example.demo.entity.ProductDetail;
import com.example.demo.model.response.common.ProductDetailDTO;
import com.example.demo.repository.common.ProductDetailCommonRepository;
import com.example.demo.repository.common.ProductCommonRepository;
import com.example.demo.service.common.ProductDetailCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDetailCommonServiceImpl implements ProductDetailCommonService {



    @Autowired
    private ProductDetailCommonRepository productDetailRepository;

    @Autowired
    private ProductCommonRepository productCommonRepository;




    public Page<ProductDetailDTO> getFilteredProductDetails(
            List<Long> colorIds, List<Long> sizeIds, List<Long> materialIds,
            List<Long> collarIds, List<Long> wristIds, List<Long> categoryIds,
            String search, Pageable pageable) {

        var specification = ProductDetailSpecificationCommon.filterByAttributesAndSearch(
                colorIds, sizeIds, materialIds, collarIds, wristIds, categoryIds, search);

        Page<ProductDetail> productDetailsPage = productDetailRepository.findAll(specification, pageable);

        return productDetailsPage.map(productDetail -> new ProductDetailDTO(
                productDetail.getId(),
                productDetail.getQuantity(),
                productDetail.getBarcode(),
                productDetail.getProduct().getPrice(),
                productCommonRepository.findPromotionalPriceByProductId((productDetail.getId())),
                productDetail.getStatus(),
                productDetail.getProduct().getName(),
                productDetail.getProduct().getCategory().getName(),
                productDetail.getProduct().getMaterial().getName(),
                productDetail.getProduct().getWrist().getName(),
                productDetail.getProduct().getCollar().getName(),
                productDetail.getSize().getName(),
                productDetail.getColor().getName()
        ));
    }


}
