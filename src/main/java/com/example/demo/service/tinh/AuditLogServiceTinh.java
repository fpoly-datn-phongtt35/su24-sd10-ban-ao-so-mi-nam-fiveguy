package com.example.demo.service.tinh;

import com.example.demo.entity.AuditLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface AuditLogServiceTinh {

    List<AuditLogs> getAll();

//    AuditLogs create(AuditLogs auditLogs);

    void createAuditLoginEmployee(String name, String text, String text2);

    void createAuditLoginCustomer(String name, String text, String text2);

    Page<AuditLogs> findAuditLog(String implementer, String code, String actionType, Date time, String detailedAction, Integer status, Pageable pageable);
}
