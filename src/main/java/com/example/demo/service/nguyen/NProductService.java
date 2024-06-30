package com.example.demo.service.nguyen;

import com.example.demo.model.response.nguyen.ProductFilterResponse;

import java.math.BigDecimal;

public interface NProductService {


    BigDecimal getMaxPrice();
    BigDecimal getMinPrice();
}
