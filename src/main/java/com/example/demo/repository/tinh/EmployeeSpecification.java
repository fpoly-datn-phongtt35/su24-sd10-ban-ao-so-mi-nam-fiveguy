package com.example.demo.repository.tinh;

import com.example.demo.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class EmployeeSpecification {
    public static Specification<Employee> hasfullName(String fullName) {
        return (root, query, criteriaBuilder) -> {
            if (fullName == null || fullName.isEmpty()) {
                return criteriaBuilder.conjunction();
            } else {
                String formattedName = "%" + fullName + "%"; // Tìm kiếm đường dẫn chứa tên

                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("name"), formattedName), // Tìm kiếm tên chính xác
                        criteriaBuilder.like(root.get("name"), "%" + fullName.replaceAll("\\s", "") + "%") // Kiếm tên kết hợp với các từ khác
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
    public static Specification<Employee> hasAccountr(String account) {
        return (root, query, criteriaBuilder) ->
                account == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.lessThanOrEqualTo(root.get("account"), account);
    }

    public static Specification<Employee> hasStatus(Integer status) {
        return (root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("status"), status);
    }
}
