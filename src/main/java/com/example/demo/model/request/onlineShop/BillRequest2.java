package com.example.demo.model.request.onlineShop;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillHistory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillRequest2 {
    Bill bill;
    BillHistory billHistory;
}
