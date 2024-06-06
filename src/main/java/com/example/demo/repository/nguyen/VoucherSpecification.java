package com.example.demo.repository.nguyen;

import com.example.demo.entity.Voucher;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class VoucherSpecification {

//    public static Specification<Voucher> hasName(String name) {
//        return (root, query, criteriaBuilder) ->
//                name == null ? criteriaBuilder.conjunction() :
//                        criteriaBuilder.equal(root.get("name"), name);
//    }
    //gpt
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

    public static Specification<Voucher> hasName1(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            } else {
                String likePattern = "%" + name.replace(" ", "%") + "%";
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern.toLowerCase());
            }
        };
    }
    public static Specification<Voucher> hasCode(String code) {
        return (root, query, criteriaBuilder) ->
                code == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("code"), code);
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
