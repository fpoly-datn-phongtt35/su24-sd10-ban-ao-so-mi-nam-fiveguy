package com.example.demo.repository.tinh;

import com.example.demo.entity.Account;
import com.example.demo.entity.Employee;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;

public class EmployeeSpecificationTinh {
    public static Specification<Employee> hasfullName(String fullName) {
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

    public static Specification<Employee> hasName1(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            } else {
                String likePattern = "%" + name.replace(" ", "%") + "%";
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern.toLowerCase());
            }
        };
    }
    public static Specification<Employee> hasCode(String code) {
        return (root, query, criteriaBuilder) ->
                code == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("code"), code);
    }

    public static Specification<Employee> hasAvatar(String avatar) {
        return (root, query, criteriaBuilder) ->
                avatar == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("avatar"), avatar);
    }

    public static Specification<Employee> hasBrithDate(Date brithDate) {
        return (root, query, criteriaBuilder) ->
                brithDate == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.greaterThanOrEqualTo(root.get("brithDate"), brithDate);
    }

    public static Specification<Employee> hasGenDer(Boolean gender) {
        return (root, query, criteriaBuilder) ->
                gender == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.lessThanOrEqualTo(root.get("gender"), gender);
    }
    public static Specification<Employee> hasAddRess(String address) {
        return (root, query, criteriaBuilder) ->
                address == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.lessThanOrEqualTo(root.get("address"), address);
    }
    public static Specification<Employee> hasAccountByAccount(String acount) {
        return (root, query, criteriaBuilder) -> {
            if (acount == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("account").get("account"), acount);
        };
    }
    public static Specification<Employee> hasAccountByEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("account").get("email"), email);
        };
    }
    public static Specification<Employee> hasAccountByPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) -> {
            if (phoneNumber == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("account").get("phoneNumber"), phoneNumber);
        };
    }

    public static Specification<Employee> hasAccountByRole(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("account").get("role").get("id"), id);
        };
    }

    public static Specification<Employee> hasStatus(Integer status) {
        return (root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("status"), status);
    }
}
