package com.example.demo.model.response.sale;

import java.math.BigDecimal;

public interface ProductDetailResponse {
     String getProductName();
     Long getTotalQuantityBought();
     BigDecimal getOriginalPrice();
     BigDecimal getPromotionalPrice();
     BigDecimal getTotalAmountBeforeDiscount();
     BigDecimal getTotalAmountAfterDiscount();
}
