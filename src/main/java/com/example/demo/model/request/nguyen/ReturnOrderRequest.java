package com.example.demo.model.request.nguyen;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.entity.ReturnOrder;
import lombok.Data;

import java.util.List;

@Data
public class ReturnOrderRequest {

    Bill bill;

    List<ReturnOrder> returnOrders;
}
