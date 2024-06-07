package com.example.demo.service.sale.serviceImpl;

import com.example.demo.entity.ProductSale;
import com.example.demo.repository.sale.ProductSaleRepository;
import com.example.demo.service.sale.ProductSaleService;
import com.example.demo.untility.ProductSaleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductSaleServiceImpl implements ProductSaleService {

    @Autowired
    private ProductSaleRepository productSaleRepository;

    @Override
    public ProductSale saveProductSale(ProductSale productSale) {
        return productSaleRepository.save(productSale);
    }

    @Override
    public ProductSale updateProductSale(Long id, ProductSale productSale) {
        Optional<ProductSale> existingProductSale = productSaleRepository.findById(id);
        if (existingProductSale.isPresent()) {
            ProductSale updatedProductSale = existingProductSale.get();
            updatedProductSale.setPromotionalPrice(productSale.getPromotionalPrice());
            updatedProductSale.setDiscountPrice(productSale.getDiscountPrice());
            updatedProductSale.setStatus(productSale.getStatus());
            updatedProductSale.setSale(productSale.getSale());
            updatedProductSale.setProduct(productSale.getProduct());
            return productSaleRepository.save(updatedProductSale);
        } else {
            throw new RuntimeException("ProductSale not found with id " + id);
        }
    }

    @Override
    public void deleteProductSale(Long id) {
        productSaleRepository.deleteById(id);
    }

    @Override
    public ProductSale getProductSaleById(Long id) {
        return productSaleRepository.findById(id).orElseThrow(() -> new RuntimeException("ProductSale not found with id " + id));
    }

    @Override
    public List<ProductSale> getAllProductSales() {
        return productSaleRepository.findAll();
    }

    @Override
    public List<ProductSale> getProductSalesBySaleId(Long saleId) {
        return productSaleRepository.findBySaleId(saleId);
    }

    @Override
    public List<ProductSale> addProductSales(List<ProductSale> productSales) {


        return productSaleRepository.saveAll(productSales);
    }


    @Override
    public void deleteProductSales(List<Long> ids) {
        productSaleRepository.deleteAllById(ids);
    }

    @Override
    public void deleteAllProductSales() {
        productSaleRepository.deleteAll();
    }

    @Override
    public Page<ProductSale> filterProductSales(Long saleId, Long productId, Long categoryId, Long collarId, Long wristId, Long colorId, Long sizeId, Long materialId, Integer status, String searchTerm, Pageable pageable) {
        Specification<ProductSale> spec = Specification.where(null);

        if (saleId != null) {
            spec = spec.and(ProductSaleSpecification.hasSale(saleId));
        }
        if (productId != null) {
            spec = spec.and(ProductSaleSpecification.hasProduct(productId));
        }
        if (categoryId != null) {
            spec = spec.and(ProductSaleSpecification.hasCategory(categoryId));
        }
        if (collarId != null) {
            spec = spec.and(ProductSaleSpecification.hasCollar(collarId));
        }
        if (wristId != null) {
            spec = spec.and(ProductSaleSpecification.hasWrist(wristId));
        }
        if (colorId != null) {
            spec = spec.and(ProductSaleSpecification.hasColor(colorId));
        }
        if (sizeId != null) {
            spec = spec.and(ProductSaleSpecification.hasSize(sizeId));
        }
        if (materialId != null) {
            spec = spec.and(ProductSaleSpecification.hasMaterial(materialId));
        }
        if (status != null) {
            spec = spec.and(ProductSaleSpecification.hasStatus(status));
        }
        if (searchTerm != null && !searchTerm.isEmpty()) {
            spec = spec.and(ProductSaleSpecification.containsSearchTerm(searchTerm));
        }

        return productSaleRepository.findAll(spec, pageable);
    }
}