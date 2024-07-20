package com.example.demo.service.tinh;

import com.example.demo.entity.BillHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface BillHistoryServiceTinh {

    List<BillHistory> getAll();

    Page<BillHistory> findHistory(Date createdAt, String createdBy, Integer type, Integer status, Pageable pageable);
}
