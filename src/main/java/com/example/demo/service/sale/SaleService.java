package com.example.demo.service.sale;

import com.example.demo.entity.Sale;
import com.example.demo.model.response.sale.SaleSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface SaleService {

    Sale saveSale(Sale sale);
    void deleteSale(Long id);
    Sale getSaleById(Long id);
    List<Sale> getAllSales();
    Long countCurrentSales();
    Long countUpcomingSales();
    Long countExpiredSales();
//    List<Sale> searchByCodeNameValue(String searchTerm);
    Page<Sale> findSalesByConditions(Date startDate, Date endDate, Integer status , String searchTerm,Integer discountTyp, Pageable pageable);

    SaleSummaryResponse getSaleSummaryById(Long saleId);
}