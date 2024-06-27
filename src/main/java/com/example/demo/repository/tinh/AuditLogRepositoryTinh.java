package com.example.demo.repository.tinh;

import com.example.demo.entity.AuditLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepositoryTinh extends JpaRepository<AuditLogs, Long> {
}
