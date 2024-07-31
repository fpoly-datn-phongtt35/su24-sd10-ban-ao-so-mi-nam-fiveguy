package com.example.demo.service.nguyen;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Employee;

import java.util.List;

public interface NSellService {

    List<Bill> getAllBillSell();

    Bill getById(Long billId);

    Bill createBillSell(Bill bill, Employee employee);

    void removeBillSell(Long billId);
}
