package com.example.demo.service.sale.serviceImpl;

import com.example.demo.entity.Product;
import com.example.demo.repository.sale.ProductRepository;
import com.example.demo.service.sale.ProductService;
import com.example.demo.untility.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getProductsWithoutSaleOrExpiredPromotion() {
        Date currentDate = new Date();
        List<Product> productsWithoutSale = productRepository.findByProductSalesIsNull();
        List<Product> productsWithExpiredPromotion = productRepository.findByProductSalesEndDateBefore(currentDate);
        List<Product> mergedProducts = new ArrayList<>(productsWithoutSale);
        mergedProducts.addAll(productsWithExpiredPromotion);
        return mergedProducts;
    }

    @Override
    public Page<Product> filterProducts(Long categoryId, Long collarId, Long wristId, Long colorId, Long sizeId, Long materialId, String searchTerm, int page, int size) {
        Specification<Product> spec = Specification.where(null);

        if (categoryId != null) {
            spec = spec.and(ProductSpecification.hasCategory(categoryId));
        }
        if (collarId != null) {
            spec = spec.and(ProductSpecification.hasCollar(collarId));
        }
        if (wristId != null) {
            spec = spec.and(ProductSpecification.hasWrist(wristId));
        }
        if (colorId != null) {
            spec = spec.and(ProductSpecification.hasColor(colorId));
        }
        if (sizeId != null) {
            spec = spec.and(ProductSpecification.hasSize(sizeId));
        }
        if (materialId != null) {
            spec = spec.and(ProductSpecification.hasMaterial(materialId));
        }
        if (searchTerm != null && !searchTerm.isEmpty()) {
            spec = spec.and(ProductSpecification.containsSearchTerm(searchTerm));
        }

        spec = spec.and(ProductSpecification.isStatus(1)); // Assuming the status filter

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return productRepository.findAll(spec, pageable);
    }


}