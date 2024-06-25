package com.example.demo.service.sale.serviceImpl;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductSale;
import com.example.demo.entity.Sale;
import com.example.demo.model.response.sale.ProductDetailResponse;
import com.example.demo.model.response.sale.SaleDetailResponse;
import com.example.demo.model.response.sale.SaleSummaryResponse;
import com.example.demo.repository.sale.ProductSaleRepository;
import com.example.demo.repository.sale.SaleRepository;
import com.example.demo.service.sale.ProductSaleService;
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

    @Autowired
    private ProductSaleRepository productSaleRepository;

    private void updateProductSales(Sale sale) {
        List<ProductSale> productSales = productSaleRepository.findBySaleId(sale.getId());

        for (ProductSale productSale : productSales) {
            Product product = productSale.getProduct();
            int discount = 0;

            if (sale.getDiscountType() == 1) {
                discount = sale.getValue();
            } else if (sale.getDiscountType() == 2) {
                discount = product.getPrice().intValue() * sale.getValue() / 100;
            }

            if (sale.getMaximumDiscountAmount() != null && discount > sale.getMaximumDiscountAmount()) {
                discount = sale.getMaximumDiscountAmount();
            }

            int promotionalPrice = product.getPrice().intValue() - discount;

            productSale.setPromotionalPrice(promotionalPrice);
            productSale.setDiscountPrice(discount);
        }

        productSaleRepository.saveAll(productSales);
    }




    @Override
    public Sale saveSale(Sale sale) {
        Date now = new Date();
        boolean isNewSale = sale.getId() == null;

        if (isNewSale) {
            sale.setCreatedAt(now); // Đặt ngày tạo mới
            Date startDate = sale.getStartDate();

            if (startDate != null && startDate.after(now)) {
                sale.setStatus(2); // Sắp bắt đầu
            } else {
                sale.setStatus(1); // Đang hoạt động
            }
        } else {
            Sale existingSale = saleRepository.findById(sale.getId()).orElseThrow(() -> new RuntimeException("Sale not found"));
//            sale.setCreatedAt(existingSale.getCreatedAt()); // Preserve the original created date
            sale.setUpdatedAt(now); // Cập nhật ngày cập nhật

            // Check if any of the relevant fields have changed
            if (!existingSale.getValue().equals(sale.getValue()) ||
                    !existingSale.getDiscountType().equals(sale.getDiscountType()) ||
                    !existingSale.getMaximumDiscountAmount().equals(sale.getMaximumDiscountAmount())) {
                updateProductSales(sale);
            }
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
    public Page<Sale> findSalesByConditions(Date startDate, Date endDate, Integer status, String searchTerm, Integer discountType, Pageable pageable) {
        Specification<Sale> spec = Specification.where(null);

        if (startDate != null && endDate != null) {
            spec = spec.and(SaleSpecifications.betweenDates(startDate, endDate));
        }

        if (status != null) {
            spec = spec.and(SaleSpecifications.hasStatus(status));
        }

        if (searchTerm != null && !searchTerm.isEmpty()) {
            spec = spec.and(SaleSpecifications.containsSearchTerm(searchTerm));
        }

        if (discountType != null) {
            spec = spec.and(SaleSpecifications.hasDiscountType(discountType));
        }

        return saleRepository.findAll(spec, pageable);
    }

    @Override
    public SaleSummaryResponse getSaleSummaryById(Long saleId) {
        return saleRepository.findSaleSummaryById(saleId);
    }

    @Override
    public List<SaleDetailResponse> findSaleDetailsById(Long saleId) {
        return saleRepository.findSaleDetailsById(saleId);
    }

    @Override
    public List<ProductDetailResponse> getProductDetailsBySaleAndCustomer(Long saleId, Long customerId) {
        return saleRepository.findProductDetailsBySaleAndCustomer(saleId, customerId);
    }


}