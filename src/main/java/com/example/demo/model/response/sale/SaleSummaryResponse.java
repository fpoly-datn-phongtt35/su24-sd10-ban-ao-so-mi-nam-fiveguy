package com.example.demo.model.response.sale;



public interface  SaleSummaryResponse {
    Long getSaleId();
    Long getTotalProductsSold();
    Double getTotalRevenue();
    Double getTotalProfit();
}
