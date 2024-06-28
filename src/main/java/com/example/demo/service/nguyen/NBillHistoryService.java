package com.example.demo.service.nguyen;

import com.example.demo.entity.BillHistory;

import java.util.List;

public interface NBillHistoryService {

    List<BillHistory> getAll();

    BillHistory getById(Long id);

    BillHistory save(BillHistory billHistory);

    List<BillHistory> getByBillId(Long id);
}
