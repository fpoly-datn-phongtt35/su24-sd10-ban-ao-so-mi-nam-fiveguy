package com.example.demo.repository.tinh;

import com.example.demo.entity.AuditLogs;
import com.example.demo.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepositoryTinh extends JpaRepository<AuditLogs, Long>, JpaSpecificationExecutor<AuditLogs> {
}
