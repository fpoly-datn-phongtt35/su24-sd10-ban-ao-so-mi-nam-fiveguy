package com.example.demo.service.common.impl;

import com.example.demo.entity.ProductDetail;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductDetailSpecificationCommon {

    public static Specification<ProductDetail> filterByAttributesAndSearch(
            List<Long> colorIds, List<Long> sizeIds, List<Long> materialIds,
            List<Long> collarIds, List<Long> wristIds, List<Long> categoryIds, String search) {

        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (colorIds != null && !colorIds.isEmpty()) {
                predicates = criteriaBuilder.and(predicates, root.get("color").get("id").in(colorIds));
            }

            if (sizeIds != null && !sizeIds.isEmpty()) {
                predicates = criteriaBuilder.and(predicates, root.get("size").get("id").in(sizeIds));
            }

            if (materialIds != null && !materialIds.isEmpty()) {
                predicates = criteriaBuilder.and(predicates, root.join("product", JoinType.INNER).get("idMaterial").in(materialIds));
            }

            if (collarIds != null && !collarIds.isEmpty()) {
                predicates = criteriaBuilder.and(predicates, root.join("product", JoinType.INNER).get("idCollar").in(collarIds));
            }

            if (wristIds != null && !wristIds.isEmpty()) {
                predicates = criteriaBuilder.and(predicates, root.join("product", JoinType.INNER).get("idWrist").in(wristIds));
            }

            if (categoryIds != null && !categoryIds.isEmpty()) {
                predicates = criteriaBuilder.and(predicates, root.join("product", JoinType.INNER).get("idCategory").in(categoryIds));
            }

            if (search != null && !search.isEmpty()) {
                var productPredicate = criteriaBuilder.like(root.join("product", JoinType.INNER).get("name"), "%" + search + "%");
                var colorPredicate = criteriaBuilder.like(root.join("color", JoinType.INNER).get("colorCode"), "%" + search + "%");
                var sizePredicate = criteriaBuilder.like(root.join("size", JoinType.INNER).get("name"), "%" + search + "%");
                var materialPredicate = criteriaBuilder.like(root.join("product", JoinType.INNER).join("material", JoinType.INNER).get("name"), "%" + search + "%");
                var collarPredicate = criteriaBuilder.like(root.join("product", JoinType.INNER).join("collar", JoinType.INNER).get("name"), "%" + search + "%");
                var wristPredicate = criteriaBuilder.like(root.join("product", JoinType.INNER).join("wrist", JoinType.INNER).get("name"), "%" + search + "%");

                predicates = criteriaBuilder.and(predicates, criteriaBuilder.or(
                        productPredicate, colorPredicate, sizePredicate,
                        materialPredicate, collarPredicate, wristPredicate
                ));
            }

            return predicates;
        };
    }

}
