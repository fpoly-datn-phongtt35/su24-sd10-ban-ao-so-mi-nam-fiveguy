package com.example.demo.service.point;

import com.example.demo.entity.CustomerPointsHistory;

import java.util.List;

public interface CustomerPointsHistoryService {
    List<CustomerPointsHistory> getPointsHistoryByCustomerId(Long customerId);
    CustomerPointsHistory savePointsHistory(CustomerPointsHistory customerPointsHistory);
    CustomerPointsHistory updatePointsHistory(CustomerPointsHistory customerPointsHistory);
}