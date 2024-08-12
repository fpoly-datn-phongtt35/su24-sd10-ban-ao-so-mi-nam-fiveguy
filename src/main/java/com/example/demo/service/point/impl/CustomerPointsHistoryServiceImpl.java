package com.example.demo.service.point.impl;

import com.example.demo.entity.CustomerPointsHistory;
import com.example.demo.repository.point.CustomerPointsHistoryRepository;
import com.example.demo.service.point.CustomerPointsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerPointsHistoryServiceImpl implements CustomerPointsHistoryService {

    private final CustomerPointsHistoryRepository repository;

    @Autowired
    public CustomerPointsHistoryServiceImpl(CustomerPointsHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CustomerPointsHistory> getPointsHistoryByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public CustomerPointsHistory savePointsHistory(CustomerPointsHistory customerPointsHistory) {
        return repository.save(customerPointsHistory);
    }

    @Override
    public CustomerPointsHistory updatePointsHistory(CustomerPointsHistory customerPointsHistory) {
        if (customerPointsHistory.getId() == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        return repository.save(customerPointsHistory);
    }
}