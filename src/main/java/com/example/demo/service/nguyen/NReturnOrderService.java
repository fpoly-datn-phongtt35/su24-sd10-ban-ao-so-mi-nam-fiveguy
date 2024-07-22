package com.example.demo.service.nguyen;

import com.example.demo.entity.ReturnOrder;

import java.util.List;

public interface NReturnOrderService {

    List<ReturnOrder> findAllReturnOrdersByBillId(Long billId);
}
