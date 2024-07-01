package com.example.demo.service.sale.serviceImpl;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductSale;
import com.example.demo.entity.Sale;
import com.example.demo.model.response.sale.ProductDetailResponse;
import com.example.demo.model.response.sale.SaleDetailResponse;
import com.example.demo.model.response.sale.SaleSummaryResponse;
import com.example.demo.repository.sale.ProductSaleRepository2;
import com.example.demo.repository.sale.SaleRepository2;
import com.example.demo.service.sale.SaleService2;
import com.example.demo.untility.SaleSpecifications2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SaleServiceImpl2 implements SaleService2 {

    @Autowired
    private SaleRepository2 saleRepository2;

    @Autowired
    private ProductSaleRepository2 productSaleRepository2;

    private void updateProductSales(Sale sale) {
        List<ProductSale> productSales = productSaleRepository2.findBySaleId(sale.getId());

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

        productSaleRepository2.saveAll(productSales);
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
            Sale existingSale = saleRepository2.findById(sale.getId()).orElseThrow(() -> new RuntimeException("Sale not found"));
//            sale.setCreatedAt(existingSale.getCreatedAt()); // Preserve the original created date
            sale.setUpdatedAt(now); // Cập nhật ngày cập nhật

            // Check if any of the relevant fields have changed
            if (!existingSale.getValue().equals(sale.getValue()) ||
                    !existingSale.getDiscountType().equals(sale.getDiscountType()) ||
                    !existingSale.getMaximumDiscountAmount().equals(sale.getMaximumDiscountAmount())) {
                updateProductSales(sale);
            }
        }

        return saleRepository2.save(sale);
    }




    @Override
    public void deleteSale(Long id) {
        saleRepository2.deleteById(id);
    }

    @Override
    public Sale getSaleById(Long id) {
        return saleRepository2.findById(id).orElseThrow(() -> new RuntimeException("Sale not found with id " + id));
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository2.findAll();
    }

    @Override
    public Long countCurrentSales() {
        Date currentDate = new Date();
        return saleRepository2.countCurrentSales(currentDate);
    }

    @Override
    public Long countUpcomingSales() {
        Date currentDate = new Date();
        return saleRepository2.countUpcomingSales(currentDate);
    }

    @Override
    public Long countExpiredSales() {
        Date currentDate = new Date();
        return saleRepository2.countExpiredSales(currentDate);
    }

    @Override
    public Page<Sale> findSalesByConditions(Date startDate, Date endDate, Integer status, String searchTerm, Integer discountType, Pageable pageable) {
        Specification<Sale> spec = Specification.where(null);

        if (startDate != null && endDate != null) {
            spec = spec.and(SaleSpecifications2.betweenDates(startDate, endDate));
        }

        if (status != null) {
            spec = spec.and(SaleSpecifications2.hasStatus(status));
        }

        if (searchTerm != null && !searchTerm.isEmpty()) {
            spec = spec.and(SaleSpecifications2.containsSearchTerm(searchTerm));
        }

        if (discountType != null) {
            spec = spec.and(SaleSpecifications2.hasDiscountType(discountType));
        }

        return saleRepository2.findAll(spec, pageable);
    }

    @Override
    public SaleSummaryResponse getSaleSummaryById(Long saleId) {
        return saleRepository2.findSaleSummaryById(saleId);
    }

    @Override
    public List<SaleDetailResponse> findSaleDetailsById(Long saleId) {
        return saleRepository2.findSaleDetailsById(saleId);
    }

    @Override
    public List<ProductDetailResponse> getProductDetailsBySaleAndCustomer(Long saleId, Long customerId) {
        return saleRepository2.findProductDetailsBySaleAndCustomer(saleId, customerId);
    }


}