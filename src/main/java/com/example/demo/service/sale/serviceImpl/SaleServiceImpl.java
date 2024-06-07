package com.example.demo.service.sale.serviceImpl;

import com.example.demo.entity.Sale;
import com.example.demo.repository.sale.SaleRepository;
import com.example.demo.service.sale.SaleService;
import com.example.demo.untility.SaleSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Override
    public Sale saveSale(Sale sale) {
        Date now = new Date();
        if (sale.getId() == null) {
            sale.setCreatedAt(now); // Đặt ngày tạo mới
            Date startDate = sale.getStartDate();

            if (startDate != null && startDate.after(now)) {
                sale.setStatus(2); // Sắp bắt đầu
            } else {
                sale.setStatus(1); // Đang hoạt động
            }
        } else {
            sale.setUpdatedAt(now); // Cập nhật ngày cập nhật
        }

        return saleRepository.save(sale);
    }



    @Override
    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }

    @Override
    public Sale getSaleById(Long id) {
        return saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found with id " + id));
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @Override
    public Long countCurrentSales() {
        Date currentDate = new Date();
        return saleRepository.countCurrentSales(currentDate);
    }

    @Override
    public Long countUpcomingSales() {
        Date currentDate = new Date();
        return saleRepository.countUpcomingSales(currentDate);
    }

    @Override
    public Long countExpiredSales() {
        Date currentDate = new Date();
        return saleRepository.countExpiredSales(currentDate);
    }

    @Override
    public Page<Sale> findSalesByConditions(Date startDate, Date endDate, Integer status, String searchTerm, Pageable pageable) {
        Specification<Sale> spec = Specification.where(null);

        if (startDate != null || endDate != null) {
            spec = spec.and(SaleSpecifications.betweenDates(startDate, endDate));
        }

        if (status != null) {
            spec = spec.and(SaleSpecifications.hasStatus(status));
        }

        if (searchTerm != null && !searchTerm.isEmpty()) {
            spec = spec.and(SaleSpecifications.containsSearchTerm(searchTerm));
        }

        return saleRepository.findAll(spec, pageable);
    }


}