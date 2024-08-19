package com.example.demo.service.point;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Customer;
import com.example.demo.entity.CustomerPointsHistory;

import java.util.List;

public interface CustomerPointsHistoryService {
    List<CustomerPointsHistory> getPointsHistoryByCustomerId(Long customerId);
    List<CustomerPointsHistory> getPointsHistoryByCustomer(Customer customer);
    CustomerPointsHistory savePointsHistory(CustomerPointsHistory customerPointsHistory);
    CustomerPointsHistory updatePointsHistory(CustomerPointsHistory customerPointsHistory);
    void addPointsFromBill(Long billId);
}