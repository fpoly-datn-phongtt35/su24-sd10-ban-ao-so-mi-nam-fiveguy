package com.example.demo.repository.nguyen;

import com.example.demo.entity.Voucher;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class VoucherSpecification {

    public static Specification<Voucher> hasCodeOrName1(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            } else {
                String formattedKeyword = "%" + StringUtils.toLowerCaseNonAccentVietnamese(keyword) + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), formattedKeyword),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), formattedKeyword)
                );
            }
        };
    }

    public static Specification<Voucher> hasCodeOrName(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            } else {
                String formattedKeyword = "%" + keyword.toLowerCase() + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), formattedKeyword),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), formattedKeyword)
                );
            }
        };
    }

    public static Specification<Voucher> hasCode(String code) {
        return (root, query, criteriaBuilder) -> {
            if (code == null || code.isEmpty()) {
                return criteriaBuilder.conjunction();
            } else {
                String formattedCode = "%" + code + "%"; // Tìm kiếm đường dẫn chứa tên

                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("code"), formattedCode), // Tìm kiếm tên chính xác
                        criteriaBuilder.like(root.get("code"), "%" + code.replaceAll("\\s", "") + "%") // Kiếm tên kết hợp với các từ khác
                );
            }
        };
    }
    public static Specification<Voucher> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            } else {
                String formattedName = "%" + name + "%"; // Tìm kiếm đường dẫn chứa tên

                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("name"), formattedName), // Tìm kiếm tên chính xác
                        criteriaBuilder.like(root.get("name"), "%" + name.replaceAll("\\s", "") + "%") // Kiếm tên kết hợp với các từ khác
                );
            }
        };
    }

    public static Specification<Voucher> hasVisibility(Integer visibility) {
        return (root, query, criteriaBuilder) ->
                visibility == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("visibility"), visibility);
    }

    public static Specification<Voucher> hasDiscountType(Integer discountType) {
        return (root, query, criteriaBuilder) ->
                discountType == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("discountType"), discountType);
    }

    public static Specification<Voucher> hasStartDate(Date startDate) {
        return (root, query, criteriaBuilder) ->
                startDate == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate);
    }

    public static Specification<Voucher> hasEndDate(Date endDate) {
        return (root, query, criteriaBuilder) ->
                endDate == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate);
    }

    public static Specification<Voucher> hasStatus(Integer status) {
        return (root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("status"), status);
    }
}
