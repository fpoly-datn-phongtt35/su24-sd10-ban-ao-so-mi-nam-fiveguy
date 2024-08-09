package com.example.demo.service.nguyen;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.entity.ReturnOrder;
import com.example.demo.model.response.nguyen.ReturnOrderSummary;

import java.util.List;

public interface NReturnOrderService {

    List<ReturnOrder> findAllReturnOrdersByBillId(Long billId);

    List<ReturnOrder> addReturnOrderAndUpdateBill(List<ReturnOrder> returnOrders, String fullName);

    ReturnOrderSummary calculateRefundSummary(Bill bill,List<ReturnOrder> returnOrders);

    ReturnOrderSummary calculateReturnOrderSummary(Long billId, List<ReturnOrder> returnOrders);

    ReturnOrderSummary calculateBillDetailSummary(Long billId, List<BillDetail> billDetails);
}
