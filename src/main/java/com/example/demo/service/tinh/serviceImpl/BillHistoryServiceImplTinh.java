package com.example.demo.service.tinh.serviceImpl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillHistory;
import com.example.demo.entity.ProductDetail;
import com.example.demo.repository.tinh.BillHistoryRepositoryTinh;
import com.example.demo.repository.tinh.BillHistorySpecificationTinh;
import com.example.demo.repository.tinh.ProductDetailSpecificationTinh;
import com.example.demo.service.tinh.BillHistoryServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class BillHistoryServiceImplTinh implements BillHistoryServiceTinh {

    @Autowired
    BillHistoryRepositoryTinh repositoryTinh;


    @Override
    public List<BillHistory> getAll(){
        return repositoryTinh.findAll() ;}

    @Override
    public Page<BillHistory> findHistory(Date createdAt, String createdBy, Integer type, Integer status, Pageable pageable) {

        Specification<BillHistory> spec = Specification.where(BillHistorySpecificationTinh.hasCreatedAt(createdAt))
                .and(BillHistorySpecificationTinh.hasCreatedBy(createdBy))
                .and(BillHistorySpecificationTinh.hasType(type))
                .and(BillHistorySpecificationTinh.hasStatusBillHistory(status));


        return repositoryTinh.findAll(spec, pageable);
    }
}
