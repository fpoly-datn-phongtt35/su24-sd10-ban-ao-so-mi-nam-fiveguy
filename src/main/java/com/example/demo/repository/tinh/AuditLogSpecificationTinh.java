package com.example.demo.repository.tinh;

import com.example.demo.entity.AuditLogs;
import com.example.demo.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class AuditLogSpecificationTinh {
    public static Specification<AuditLogs> hasImplementer(String implementer) {
        return (root, query, criteriaBuilder) -> {
            if (implementer == null || implementer.isEmpty()) {
                return criteriaBuilder.conjunction();
            } else {
                String formattedName = "%" + implementer + "%"; // Tìm kiếm đường dẫn chứa tên

                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("implementer"), formattedName), // Tìm kiếm tên chính xác
                        criteriaBuilder.like(root.get("implementer"), "%" + implementer.replaceAll("\\s", "") + "%") // Kiếm tên kết hợp với các từ khác
                );
            }
        };
    }

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
    public static Specification<AuditLogs> hasEmpCode(String empCode) {
        return (root, query, criteriaBuilder) ->
                empCode == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("empCode"), empCode);
    }

    public static Specification<AuditLogs> hasActionType(String actionType) {
        return (root, query, criteriaBuilder) ->
                actionType == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("actionType"), actionType);
    }

    public static Specification<AuditLogs> hasTime(Date time) {
        return (root, query, criteriaBuilder) ->
                time == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.greaterThanOrEqualTo(root.get("time"), time);
    }

    public static Specification<AuditLogs> hasDetailedAction(String detailedAction) {
        return (root, query, criteriaBuilder) ->
                detailedAction == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.lessThanOrEqualTo(root.get("detailedAction"), detailedAction);
    }


    public static Specification<AuditLogs> hasStatus(Integer status) {
        return (root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("status"), status);
    }
}
