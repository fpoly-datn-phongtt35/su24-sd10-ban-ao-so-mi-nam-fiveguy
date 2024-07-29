package com.example.demo.service.onlineShop;

import com.example.demo.entity.BillHistory;

import java.util.List;

public interface OLBillHistoryService2 {

    BillHistory save (BillHistory billHistory);
    List<BillHistory> getBillHistoriesByBillId(Long billId);


}
