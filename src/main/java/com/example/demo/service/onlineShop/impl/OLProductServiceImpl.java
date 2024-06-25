package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.Product;
import com.example.demo.model.response.onlineShop.ProductSaleDetails;
import com.example.demo.repository.onlineShop.OLProductRepository;
import com.example.demo.service.onlineShop.OLProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OLProductServiceImpl implements OLProductService {


    @Autowired
    private OLProductRepository productRepository;

    // Phương thức để lọc sản phẩm dựa trên các thuộc tính và phân trang, sắp xếp


    @Override
    public Page<ProductSaleDetails> filterProducts(
            String name,
            Set<String> colorNames,
            Set<String> sizeNames,
            Set<String> materialNames,
            Set<String> collarNames,
            Set<String> wristNames,
            Double minPrice,
            Double maxPrice,
            Pageable pageable) {

        List<Object[]> results = productRepository.findProductsOnSaleWithFilter(name, minPrice, maxPrice, pageable);

        List<ProductSaleDetails> productSaleDetails = results.stream().map(result -> new ProductSaleDetails(
                ((Number) result[0]).longValue(),               // productId
                (String) result[1],                             // productName
                (BigDecimal) result[2],                         // productPrice
                (Integer) result[3],                         // discountPrice
                (Integer) result[4],                         // promotionalPrice
                (Integer) result[5],                         // saleValue
                (Integer) result[6]                              // discountType
        )).collect(Collectors.toList());

        return new PageImpl<>(productSaleDetails, pageable, productSaleDetails.size());
    }

    // Other existing methods for filtering

    // Existing specification methods

    private Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    private Specification<Product> hasColors(Set<String> colorNames) {
        return (root, query, criteriaBuilder) -> root.join("productDetails").join("color").get("name").in(colorNames);
    }

    private Specification<Product> hasSizes(Set<String> sizeNames) {
        return (root, query, criteriaBuilder) -> root.join("productDetails").join("size").get("name").in(sizeNames);
    }

    private Specification<Product> hasMaterials(Set<String> materialNames) {
        return (root, query, criteriaBuilder) -> root.join("material").get("name").in(materialNames);
    }

    private Specification<Product> hasCollars(Set<String> collarNames) {
        return (root, query, criteriaBuilder) -> root.join("collar").get("name").in(collarNames);
    }

    private Specification<Product> hasWrists(Set<String> wristNames) {
        return (root, query, criteriaBuilder) -> root.join("wrist").get("name").in(wristNames);
    }

    private Specification<Product> hasMinPrice(Double minPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    private Specification<Product> hasMaxPrice(Double maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}
