package com.example.demo.service.tinh.serviceImpl;

import com.example.demo.entity.AuditLogs;
import com.example.demo.entity.Employee;
import com.example.demo.repository.tinh.AuditLogRepositoryTinh;
import com.example.demo.service.tinh.AuditLogServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogServiceImplTinh implements AuditLogServiceTinh {
    @Autowired
    AuditLogRepositoryTinh auditLogRepositoryTinh;


    @Override
    public List<AuditLogs> getAll(){
        return auditLogRepositoryTinh.findAll();
    }

}
