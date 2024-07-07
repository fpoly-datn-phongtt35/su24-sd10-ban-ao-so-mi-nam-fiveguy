package com.example.demo.repository.tinh;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.Employee;
import com.example.demo.entity.ProductDetail;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Date;

public class ProductDetailSpecificationTinh {
//    public static Specification<Employee> hasfullName(String name) {
//        return (root, query, criteriaBuilder) -> {
//            if (name == null || name.isEmpty()) {
//                return criteriaBuilder.conjunction();
//            } else {
//                String formattedName = "%" + name + "%"; // Tìm kiếm đường dẫn chứa tên
//
//                return criteriaBuilder.or(
//                        criteriaBuilder.like(root.get("name"), formattedName), // Tìm kiếm tên chính xác
//                        criteriaBuilder.like(root.get("product"), "%" + fullName.replaceAll("\\s", "") + "%") // Kiếm tên kết hợp với các từ khác
//                );
//            }
//        };
//    }

//    public static Specification<Employee> hasName1(String name) {
//        return (root, query, criteriaBuilder) -> {
//            if (name == null || name.isEmpty()) {
//                return criteriaBuilder.conjunction();
//            } else {
//                String likePattern = "%" + name.replace(" ", "%") + "%";
//                return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern.toLowerCase());
//            }
//        };
//    }
    public static Specification<ProductDetail> hasCode(String code) {
        return (root, query, criteriaBuilder) ->
                code == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("product").get("code"), code);
    }

    public static Specification<ProductDetail> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("product").get("name"), name);
    }

    public static Specification<ProductDetail> hasPrice(BigDecimal price) {
        return (Root<ProductDetail> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (price == null) {
                return criteriaBuilder.conjunction(); // Không có điều kiện nào nếu giá là null
            } else {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("product").get("price"), price);
            }
        };
    }

//    public static Specification<ProductDetail> hasGenDer(Boolean gender) {
//        return (root, query, criteriaBuilder) ->
//                gender == null ? criteriaBuilder.conjunction() :
//                        criteriaBuilder.lessThanOrEqualTo(root.get("gender"), gender);
//    }
//    public static Specification<ProductDetail> hasAddRess(String address) {
//        return (root, query, criteriaBuilder) ->
//                address == null ? criteriaBuilder.conjunction() :
//                        criteriaBuilder.lessThanOrEqualTo(root.get("address"), address);
//    }
//    public static Specification<ProductDetail> hasAccountByAccount(String acount) {
//        return (root, query, criteriaBuilder) -> {
//            if (acount == null) {
//                return criteriaBuilder.conjunction();
//            }
//            return criteriaBuilder.equal(root.get("account").get("account"), acount);
//        };
//    }
//    public static Specification<ProductDetail> hasAccountByEmail(String email) {
//        return (root, query, criteriaBuilder) -> {
//            if (email == null) {
//                return criteriaBuilder.conjunction();
//            }
//            return criteriaBuilder.equal(root.get("account").get("email"), email);
//        };
//    }
//    public static Specification<ProductDetail> hasAccountByPhoneNumber(String phoneNumber) {
//        return (root, query, criteriaBuilder) -> {
//            if (phoneNumber == null) {
//                return criteriaBuilder.conjunction();
//            }
//            return criteriaBuilder.equal(root.get("account").get("phoneNumber"), phoneNumber);
//        };
//    }
//
//    public static Specification<ProductDetail> hasAccountByRole(Long id) {
//        return (root, query, criteriaBuilder) -> {
//            if (id == null) {
//                return criteriaBuilder.conjunction();
//            }
//            return criteriaBuilder.equal(root.get("account").get("role").get("id"), id);
//        };
//    }
//
//    public static Specification<ProductDetail> hasStatus(Integer status) {
//        return (root, query, criteriaBuilder) ->
//                status == null ? criteriaBuilder.conjunction() :
//                        criteriaBuilder.equal(root.get("status"), status);
//    }
}
