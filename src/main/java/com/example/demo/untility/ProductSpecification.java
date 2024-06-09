//package com.example.demo.untility;
//
//
//import com.example.demo.entity.Product;
//import com.example.demo.entity.ProductDetail;
//import jakarta.persistence.criteria.Join;
//import org.springframework.data.jpa.domain.Specification;
//
//
//public class ProductSpecification {
//
//    public static Specification<Product> hasCategory(Long categoryId) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"), categoryId);
//    }
//
//    public static Specification<Product> hasCollar(Long collarId) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("collar").get("id"), collarId);
//    }
//
//    public static Specification<Product> hasWrist(Long wristId) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("wrist").get("id"), wristId);
//    }
//
//    public static Specification<Product> hasColor(Long colorId) {
//        return (root, query, criteriaBuilder) -> {
//            Join<Product, ProductDetail> productDetailJoin = root.join("productDetails");
//            return criteriaBuilder.equal(productDetailJoin.get("color").get("id"), colorId);
//        };
//    }
//
//    public static Specification<Product> hasSize(Long sizeId) {
//        return (root, query, criteriaBuilder) -> {
//            Join<Product, ProductDetail> productDetailJoin = root.join("productDetails");
//            return criteriaBuilder.equal(productDetailJoin.get("size").get("id"), sizeId);
//        };
//    }
//
//    public static Specification<Product> hasMaterial(Long materialId) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("material").get("id"), materialId);
//    }
//
//    public static Specification<Product> isStatus(Integer status) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
//    }
//
//    public static Specification<Product> containsSearchTerm(String searchTerm) {
//        return (root, query, criteriaBuilder) -> {
//            String likePattern = "%" + searchTerm.toLowerCase() + "%";
//            return criteriaBuilder.or(
//                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
//                    criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), likePattern)
//            );
//        };
//    }
//}
