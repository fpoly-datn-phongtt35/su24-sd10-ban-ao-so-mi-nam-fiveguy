package com.example.demo.service.tinh;

import com.example.demo.entity.Bill;

import java.util.List;

public interface BillServiceTinh {
//    List<Bill> getAll();

    Bill create(Bill bill);
    void updateBillStatus(Long id);
}
