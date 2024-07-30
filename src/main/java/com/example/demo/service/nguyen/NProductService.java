package com.example.demo.service.nguyen;

import com.example.demo.entity.Image;
import com.example.demo.model.response.nguyen.ProductFilterResponse;

import java.math.BigDecimal;

public interface NProductService {


    BigDecimal getMaxPrice();
    BigDecimal getMinPrice();

    String getImagePathByProductId(Long id, Long colorId);
}
