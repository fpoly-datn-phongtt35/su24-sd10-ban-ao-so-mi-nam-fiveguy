package com.example.demo.service.tinh.serviceImpl;

import com.example.demo.entity.AuditLogs;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Employee;
import com.example.demo.repository.Customer.CustomerRepositoryH;
import com.example.demo.repository.tinh.AuditLogRepositoryTinh;
import com.example.demo.repository.tinh.AuditLogSpecificationTinh;
import com.example.demo.repository.tinh.EmployeeRepositoryTinh;
import com.example.demo.repository.tinh.EmployeeSpecificationTinh;
import com.example.demo.service.tinh.AuditLogServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AuditLogServiceImplTinh implements AuditLogServiceTinh {
    @Autowired
   private AuditLogRepositoryTinh auditLogRepositoryTinh;

    @Autowired private EmployeeServiceImplTinh employeeServiceImplTinh;

    @Autowired
    private CustomerRepositoryH customerRepositoryH;

    @Autowired
    private EmployeeRepositoryTinh employeeRepositoryTinh;

    @Override
    public List<AuditLogs> getAll(){
        return auditLogRepositoryTinh.findAll();
    }


    @Override
    public void createAuditLoginEmployee(String name, String text, String text2){
        Optional<Employee> employee = employeeRepositoryTinh.findByFullName(name);

        if(employee.isPresent()){
            AuditLogs auditLogs1= new AuditLogs();
            auditLogs1.setEmpCode(employee.get().getCode());
            auditLogs1.setImplementer(employee.get().getFullName());
            auditLogs1.setActionType(text);
            auditLogs1.setDetailedAction(text2);
            auditLogs1.setTime(new Date());
            auditLogs1.setStatus(1);

           auditLogRepositoryTinh.save(auditLogs1);
        }
    }

    @Override
    public void createAuditLoginCustomer(String name, String text, String text2) {
        Optional<Customer> customer = customerRepositoryH.findByFullName(name);
        if(customer.isPresent()){
            AuditLogs auditLogs1= new AuditLogs();
            auditLogs1.setEmpCode(customer.get().getCode());
            auditLogs1.setImplementer(customer.get().getFullName());
            auditLogs1.setActionType(text);
            auditLogs1.setDetailedAction(text2);
            auditLogs1.setTime(new Date());
            auditLogs1.setStatus(2);

            auditLogRepositoryTinh.save(auditLogs1);
        }
    }

    @Override
    public Page<AuditLogs> findAuditLog(String implementer, String code, String actionType, Date time, String detailedAction,  Integer status, Pageable pageable) {

        Specification<AuditLogs> spec = Specification.where(AuditLogSpecificationTinh.hasEmpCode(code))
                .and(AuditLogSpecificationTinh.hasImplementer(implementer))
                .and(AuditLogSpecificationTinh.hasActionType(actionType))
                .and(AuditLogSpecificationTinh.hasDetailedAction(detailedAction))
                .and(AuditLogSpecificationTinh.hasTime(time))
                .and(AuditLogSpecificationTinh.hasStatus(status));

        return auditLogRepositoryTinh.findAll(spec, pageable);
    }


}
