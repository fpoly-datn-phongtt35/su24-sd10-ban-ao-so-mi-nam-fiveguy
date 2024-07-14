package com.example.demo.service.sale.serviceImpl;

import com.example.demo.entity.ProductSale;
import com.example.demo.repository.sale.ProductSaleRepository2;
import com.example.demo.service.sale.ProductSaleService2;
import com.example.demo.untility.ProductSaleSpecification2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductSaleServiceImpl2 implements ProductSaleService2 {

    @Autowired
    private ProductSaleRepository2 productSaleRepository2;

//    @Autowired
//    private ProductService productService;
//
//    @Autowired
//    private SaleService saleService;

    @Override
    public ProductSale saveProductSale(ProductSale productSale) {
        return productSaleRepository2.save(productSale);
    }

    @Override
    public ProductSale updateProductSale(Long id, ProductSale productSale) {
        Optional<ProductSale> existingProductSale = productSaleRepository2.findById(id);
        if (existingProductSale.isPresent()) {
            ProductSale updatedProductSale = existingProductSale.get();
            updatedProductSale.setPromotionalPrice(productSale.getPromotionalPrice());
            updatedProductSale.setDiscountPrice(productSale.getDiscountPrice());
            updatedProductSale.setStatus(productSale.getStatus());
            updatedProductSale.setSale(productSale.getSale());
            updatedProductSale.setProduct(productSale.getProduct());
            return productSaleRepository2.save(updatedProductSale);
        } else {
            throw new RuntimeException("ProductSale not found with id " + id);
        }
    }

    @Override
    public void deleteProductSale(Long id) {
        productSaleRepository2.deleteById(id);
    }

    @Override
    public ProductSale getProductSaleById(Long id) {
        return productSaleRepository2.findById(id).orElseThrow(() -> new RuntimeException("ProductSale not found with id " + id));
    }

    @Override
    public List<ProductSale> getAllProductSales() {
        return productSaleRepository2.findAll();
    }

    @Override
    public List<ProductSale> getProductSalesBySaleId(Long saleId) {
        return productSaleRepository2.findBySaleId(saleId);
    }

    @Override
    public List<ProductSale> addProductSales(List<ProductSale> productSales) {


        return productSaleRepository2.saveAll(productSales);
    }


    @Override
    public void deleteProductSales(List<Long> ids) {
        productSaleRepository2.deleteAllById(ids);
    }

//    @Override
//    public void deleteAllProductSales() {
//        productSaleRepository.deleteAll();
//    }

    @Override
    public Page<ProductSale> filterProductSales(Long saleId, Long productId, Long categoryId, Long collarId, Long wristId, Long colorId, Long sizeId, Long materialId, Integer status, String searchTerm, Pageable pageable) {
        Specification<ProductSale> spec = Specification.where(null);

        if (saleId != null) {
            spec = spec.and(ProductSaleSpecification2.hasSale(saleId));
        }
        if (productId != null) {
            spec = spec.and(ProductSaleSpecification2.hasProduct(productId));
        }
        if (categoryId != null) {
            spec = spec.and(ProductSaleSpecification2.hasCategory(categoryId));
        }
        if (collarId != null) {
            spec = spec.and(ProductSaleSpecification2.hasCollar(collarId));
        }
        if (wristId != null) {
            spec = spec.and(ProductSaleSpecification2.hasWrist(wristId));
        }
        if (colorId != null) {
            spec = spec.and(ProductSaleSpecification2.hasColor(colorId));
        }
        if (sizeId != null) {
            spec = spec.and(ProductSaleSpecification2.hasSize(sizeId));
        }
        if (materialId != null) {
            spec = spec.and(ProductSaleSpecification2.hasMaterial(materialId));
        }
        if (status != null) {
            spec = spec.and(ProductSaleSpecification2.hasStatus(status));
        }
        if (searchTerm != null && !searchTerm.isEmpty()) {
            spec = spec.and(ProductSaleSpecification2.containsSearchTerm(searchTerm));
        }

        return productSaleRepository2.findAll(spec, pageable);
    }

//    @Override
//    public List<ProductSale> addAllProductSales(Long saleId) {
//        Date now = new Date();
//
//        // Find the sale by ID
//        Sale sale = saleService.getSaleById(saleId);
//
//        // Get the products without sale or with expired sale
//        List<Product> products = productService.getProductsWithoutSaleOrExpiredPromotion();
//
//        // Create a list of ProductSale
//        List<ProductSale> productSales = new ArrayList<>();
//        for (Product product : products) {
//            int discount = 0;
//            if (sale.getDiscountType() == 1) {
//                discount = sale.getValue();
//            } else if (sale.getDiscountType() == 2) {
//                discount = product.getPrice().intValue() * sale.getValue() / 100;
//            }
//            if (sale.getMaximumDiscountAmount() != null && discount > sale.getMaximumDiscountAmount()) {
//                discount = sale.getMaximumDiscountAmount();
//            }
//            int promotionalPrice = product.getPrice().intValue() - discount;
//
//            ProductSale productSale = new ProductSale();
//            productSale.setCreatedAt(now);
//
//            productSale.setProduct(product);
//            productSale.setSale(sale);
//            productSale.setPromotionalPrice(promotionalPrice);
//            productSale.setDiscountPrice(discount);
//            productSales.add(productSale);
//        }
//
//        // Save all productSales
//        return productSaleRepository.saveAll(productSales);
//    }

}