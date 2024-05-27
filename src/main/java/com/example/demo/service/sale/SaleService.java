package com.example.demo.service.sale;

import com.example.demo.entity.Sale;

import java.util.Date;
import java.util.List;

public interface SaleService {
    Sale saveSale(Sale sale);
    Sale updateSale(Long id, Sale sale);
    void deleteSale(Long id);
    Sale getSaleById(Long id);
    List<Sale> getAllSales();

    Long countCurrentSales();
    Long countUpcomingSales();
    Long countExpiredSales();
}