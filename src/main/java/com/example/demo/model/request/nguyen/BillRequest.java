package com.example.demo.model.request.nguyen;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillHistory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillRequest {
    Bill bill;
    BillHistory billHistory;
}
