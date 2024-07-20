package com.example.demo.repository.tinh;

import com.example.demo.entity.BillHistory;
import com.example.demo.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class BillHistorySpecificationTinh {
    public static Specification<BillHistory> hasFullName(String fullName) {
        return (root, query, criteriaBuilder) -> {
            if (fullName == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("bill").get("employee").get("fullName"), fullName);
        };
    }

    public static Specification<BillHistory> hasType(Integer type) {
        return (root, query, criteriaBuilder) ->
                type == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("type"), type);
    }

    public static Specification<BillHistory> hasCreatedAt(Date createdAt) {
        return (root, query, criteriaBuilder) ->
                createdAt == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdAt);
    }

    public static Specification<BillHistory> hasCreatedBy(String createdBy) {
        return (root, query, criteriaBuilder) ->
                createdBy == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("createdBy"), createdBy);
    }
//    public static Specification<BillHistory> hasStatus(Integer status) {
//        return (root, query, criteriaBuilder) ->
//                status == null ? criteriaBuilder.conjunction() :
//                        criteriaBuilder.equal(root.get("status"), status);
//    }
    public static Specification<BillHistory> hasStatusBillHistory(Integer status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("bill").get("status"), status);
        };
    }
}
