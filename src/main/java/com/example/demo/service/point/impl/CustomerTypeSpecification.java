package com.example.demo.service.point.impl;

import com.example.demo.entity.CustomerType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class CustomerTypeSpecification {

    public static Specification<CustomerType> byName(String name) {
        return (Root<CustomerType> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction(); // Return all results
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
}