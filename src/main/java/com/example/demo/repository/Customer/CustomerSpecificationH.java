package com.example.demo.repository.Customer;

import com.example.demo.entity.Customer;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public interface CustomerSpecificationH {
    public static Specification<Customer> hasfullName(String fullName) {
        return (root, query, criteriaBuilder) -> {
            if (fullName == null || fullName.isEmpty()) {
                return criteriaBuilder.conjunction();
            } else {
                String formattedName = "%" + fullName + "%"; // Tìm kiếm đường dẫn chứa tên

                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("fullName"), formattedName), // Tìm kiếm tên chính xác
                        criteriaBuilder.like(root.get("fullName"), "%" + fullName.replaceAll("\\s", "") + "%") // Kiếm tên kết hợp với các từ khác
                );
            }
        };
    }

    public static Specification<Customer> hasName1(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            } else {
                String likePattern = "%" + name.replace(" ", "%") + "%";
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern.toLowerCase());
            }
        };
    }
    public static Specification<Customer> hasCode(String code) {
        return (root, query, criteriaBuilder) ->
                code == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("code"), code);
    }

    public static Specification<Customer> hasAvatar(String avatar) {
        return (root, query, criteriaBuilder) ->
                avatar == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("avatar"), avatar);
    }

    public static Specification<Customer> hasBrithDate(Date brithDate) {
        return (root, query, criteriaBuilder) ->
                brithDate == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.greaterThanOrEqualTo(root.get("brithDate"), brithDate);
    }

    public static Specification<Customer> hasGenDer(Boolean gender) {
        return (root, query, criteriaBuilder) ->
                gender == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.lessThanOrEqualTo(root.get("gender"), gender);
    }
    public static Specification<Customer> hasAddRess(String address) {
        return (root, query, criteriaBuilder) ->
                address == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.lessThanOrEqualTo(root.get("address"), address);
    }
    public static Specification<Customer> hasAccountByAccount(String acount) {
        return (root, query, criteriaBuilder) -> {
            if (acount == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("account").get("account"), acount);
        };
    }
    public static Specification<Customer> hasAccountByEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("account").get("email"), email);
        };
    }
    public static Specification<Customer> hasAccountByPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) -> {
            if (phoneNumber == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("account").get("phoneNumber"), phoneNumber);
        };
    }

    public static Specification<Customer> hasCustomerType(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("customerType").get("name"), name);
        };
    }

    public static Specification<Customer> hasStatus(Integer status) {
        return (root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("status"), status);
    }
}
