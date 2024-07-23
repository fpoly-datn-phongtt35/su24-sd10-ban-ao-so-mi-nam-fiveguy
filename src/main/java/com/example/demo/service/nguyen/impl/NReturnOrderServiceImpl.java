package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.ReturnOrder;
import com.example.demo.repository.nguyen.bill.NReturnOrderRepository;
import com.example.demo.service.nguyen.NReturnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NReturnOrderServiceImpl implements NReturnOrderService {

    @Autowired
    NReturnOrderRepository returnOrderRepository;

    @Override
    public List<ReturnOrder> findAllReturnOrdersByBillId(Long billId) {
        return returnOrderRepository.findAllReturnOrdersByBillIdOrderByCreatedAtDesc(billId);
    }
}
