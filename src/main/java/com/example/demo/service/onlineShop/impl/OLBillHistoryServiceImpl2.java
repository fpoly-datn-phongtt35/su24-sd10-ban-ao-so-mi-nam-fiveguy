package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.BillHistory;
import com.example.demo.entity.PaymentStatus;
import com.example.demo.repository.onlineShop.OLBillHistoryRepository2;
import com.example.demo.repository.onlineShop.OLPaymentStatusRepository2;
import com.example.demo.service.onlineShop.OLBillHistoryService2;
import com.example.demo.service.onlineShop.OLPaymentStatusService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OLBillHistoryServiceImpl2 implements OLBillHistoryService2 {

    @Autowired
    private OLBillHistoryRepository2 olBillHistoryRepository2;



    @Override
    public BillHistory save(BillHistory billHistory) {
        billHistory.setCreatedAt(new Date());
        return olBillHistoryRepository2.save(billHistory);
    }

    @Override
    public List<BillHistory> getBillHistoriesByBillId(Long billId) {
        return olBillHistoryRepository2.findByBillId(billId);
    }
}
