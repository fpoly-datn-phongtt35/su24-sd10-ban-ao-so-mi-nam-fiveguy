package com.example.demo.service.sale.serviceImpl;

import com.example.demo.entity.Product;
import com.example.demo.repository.sale.ProductRepository;
import com.example.demo.service.sale.ProductService;
import com.example.demo.untility.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Product> filterProducts(Long categoryId, Long collarId, Long wristId, Long colorId, Long sizeId, Long materialId) {

        List<Product> productsWithoutSaleOrExpiredPromotion = getProductsWithoutSaleOrExpiredPromotion();

        return productsWithoutSaleOrExpiredPromotion.stream()
                .filter(product -> (categoryId == null || product.getCategory().getId().equals(categoryId)))
                .filter(product -> (collarId == null || product.getCollar().getId().equals(collarId)))
                .filter(product -> (wristId == null || product.getWrist().getId().equals(wristId)))
                .filter(product -> (colorId == null || product.getProductDetails().stream().anyMatch(detail -> detail.getColor().getId().equals(colorId))))
                .filter(product -> (sizeId == null || product.getProductDetails().stream().anyMatch(detail -> detail.getSize().getId().equals(sizeId))))
                .filter(product -> (materialId == null || product.getMaterial().getId().equals(materialId)))
                .filter(product -> product.getStatus().equals(1)) // Assuming the status filter
                .collect(Collectors.toList());
    }


    @Override
    public List<Product> searchByNameOrCode(String searchTerm) {
        return productRepository.searchByNameOrCode(searchTerm);
    }

}