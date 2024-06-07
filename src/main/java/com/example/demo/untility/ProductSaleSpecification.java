package com.example.demo.untility;


import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import com.example.demo.entity.ProductSale;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ProductSaleSpecification {

    public static Specification<ProductSale> hasSale(Long saleId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("sale").get("id"), saleId);
    }

    public static Specification<ProductSale> hasProduct(Long productId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("product").get("id"), productId);
    }

    public static Specification<ProductSale> hasCategory(Long categoryId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("product").get("category").get("id"), categoryId);
    }

    public static Specification<ProductSale> hasCollar(Long collarId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("product").get("collar").get("id"), collarId);
    }

    public static Specification<ProductSale> hasWrist(Long wristId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("product").get("wrist").get("id"), wristId);
    }

    public static Specification<ProductSale> hasColor(Long colorId) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductDetail> productDetailJoin = root.join("product").join("productDetails");
            return criteriaBuilder.equal(productDetailJoin.get("color").get("id"), colorId);
        };
    }

    public static Specification<ProductSale> hasSize(Long sizeId) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductDetail> productDetailJoin = root.join("product").join("productDetails");
            return criteriaBuilder.equal(productDetailJoin.get("size").get("id"), sizeId);
        };
    }

    public static Specification<ProductSale> hasMaterial(Long materialId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("product").get("material").get("id"), materialId);
    }

    public static Specification<ProductSale> hasStatus(Integer status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<ProductSale> containsSearchTerm(String searchTerm) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("product").get("name")), "%" + searchTerm.toLowerCase() + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("product").get("code")), "%" + searchTerm.toLowerCase() + "%")
        );
    }
}