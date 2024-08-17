package com.example.demo.repository.tinh;

import com.example.demo.entity.AuditLogs;
import com.example.demo.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AuditLogSpecificationTinh {
    public static Specification<AuditLogs> hasEmpCode(String code) {
        return (root, query, criteriaBuilder) -> {
            if (code == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("empCode"), code);
        };
    }

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

    public static Specification<AuditLogs> hasActionType(String actionType) {
        return (root, query, criteriaBuilder) -> {
            if (actionType == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("actionType"), actionType);
        };
    }

    public static Specification<AuditLogs> hasDetailedAction(String detailedAction) {
        return (root, query, criteriaBuilder) -> {
            if (detailedAction == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("detailedAction"), detailedAction);
        };
    }

    public static Specification<AuditLogs> hasTime(Date time) {
        return (root, query, criteriaBuilder) -> {
            if (time == null) {
                return criteriaBuilder.conjunction(); // Trả về tất cả nếu không có thời gian
            }

            // Chuyển đổi Date thành LocalDate
            LocalDate localDate = time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Tạo LocalDateTime cho ngày bắt đầu và kết thúc
            LocalDateTime startDateTime = localDate.atStartOfDay(); // Ngày bắt đầu: 00:00:00
            LocalDateTime endDateTime = localDate.plusDays(1).atStartOfDay(); // Ngày kết thúc: 00:00:00 ngày sau

            // Chuyển đổi LocalDateTime thành Date
            Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

            // Sử dụng `CriteriaBuilder` để tìm các bản ghi trong khoảng thời gian
            return criteriaBuilder.between(root.get("time"), startDate, endDate);
        };
    }

    public static Specification<AuditLogs> hasStatus(Integer status) {
        return (root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("status"), status);
    }

        public static Specification<AuditLogs> hasStatusIn(List<Integer> statuses) {
            return (root, query, criteriaBuilder) -> {
                if (statuses == null || statuses.isEmpty()) {
                    return criteriaBuilder.conjunction(); // Return all records if no statuses provided
                }
                return root.get("status").in(statuses);
            };
        }


}
