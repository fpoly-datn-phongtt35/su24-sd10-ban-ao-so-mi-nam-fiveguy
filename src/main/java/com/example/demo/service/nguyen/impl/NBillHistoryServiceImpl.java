package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.BillHistory;
import com.example.demo.repository.nguyen.NBillHistoryRepository;
import com.example.demo.service.nguyen.NBillHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NBillHistoryServiceImpl implements NBillHistoryService {

    @Autowired
    NBillHistoryRepository billHistoryRepository;

    @Override
    public List<BillHistory> getAll() {
        return billHistoryRepository.findAll();
    }

    @Override
    public BillHistory getById(Long id) {
        return billHistoryRepository.findById(id).get();
    }

    @Override
    public BillHistory save(BillHistory billHistory) {
        billHistory.setCreatedAt(new Date());
        return billHistoryRepository.save(billHistory);
    }

    @Override
    public BillHistory getByBillId(Long id) {
        return billHistoryRepository.findBillHistoryByBillId(id);
    }
}
