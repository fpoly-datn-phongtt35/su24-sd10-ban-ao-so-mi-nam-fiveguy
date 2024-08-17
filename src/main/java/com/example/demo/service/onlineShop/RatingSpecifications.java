package com.example.demo.service.onlineShop;

import com.example.demo.entity.Rating;
import org.springframework.data.jpa.domain.Specification;

public class RatingSpecifications {

    public static Specification<Rating> hasApprovalStatus(Integer approvalStatus) {
        return (root, query, criteriaBuilder) -> {
            if (approvalStatus == 0) {
                return criteriaBuilder.conjunction(); // No filtering by approvalStatus
            }
            return criteriaBuilder.equal(root.get("approvalStatus"), approvalStatus);
        };
    }

    public static Specification<Rating> hasSearch(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.conjunction(); // No filtering by search
            }
            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("customer").get("fullName"), "%" + search + "%"),
                    criteriaBuilder.like(root.get("reviewer"), "%" + search + "%"),
                    criteriaBuilder.like(root.join("billDetail").join("bill").get("code"), "%" + search + "%"),
                    criteriaBuilder.like(root.join("billDetail").join("productDetail").join("product").get("name"), "%" + search + "%")
            );
        };
    }
}
