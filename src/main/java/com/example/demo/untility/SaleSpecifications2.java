package com.example.demo.untility;

import com.example.demo.entity.Sale;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class SaleSpecifications2 {

    public static Specification<Sale> betweenDates(Date startDate, Date endDate) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.between(root.get("startDate"), startDate, endDate);
        };
    }

    public static Specification<Sale> hasStatus(Integer status) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Sale> containsSearchTerm(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            String likePattern = "%" + searchTerm.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern)
            );
        };
    }

    public static Specification<Sale> hasDiscountType(Integer discountType) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("discountType"), discountType);
        };
    }
}


