package com.example.demo.model.response.sale;

public interface SaleDetailResponse {
    String getCustomerName();
    String getCustomerId();
    String getSaleId();
    String getCustomerPhone();
    String getCustomerEmail();
    int getNumberOfPurchases();
    double getTotalAmountBeforeDiscount();
    double getTotalAmountAfterDiscount();
    double getTotalDiscountAmount();
}
