package com.example.demo.repository.nguyen;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Customer;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillSpecification {

    public static Specification<Bill> hasCode(String code) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("code"), code);
    }

    public static Specification<Bill> hasCustomerName(String customerName) {
        return (root, query, criteriaBuilder) -> {
            Join<Bill, Customer> customerJoin = root.join("customer");
            return criteriaBuilder.like(customerJoin.get("fullName"), "%" + customerName + "%");
        };
    }

    public static Specification<Bill> hasPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber);
    }

    public static Specification<Bill> hasTypeBill(int typeBill) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("typeBill"), typeBill);
    }

    public static Specification<Bill> hasStatus(int status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Bill> createdAtBetween(Date startDate, Date endDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
    }
}