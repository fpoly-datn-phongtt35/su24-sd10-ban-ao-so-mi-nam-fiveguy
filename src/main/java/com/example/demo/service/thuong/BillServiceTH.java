package com.example.demo.service.thuong;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Employee;
import com.example.demo.model.response.thuong.BillResponseTH;

import java.math.BigDecimal;
import java.util.List;

public interface BillServiceTH {
    List<BillResponseTH> findAllByStatusAndTypeBill(Integer status, Integer typeBill);
    BillResponseTH getOne(Long id);
    BillResponseTH addProductCart(BillResponseTH bill, Long id);
    BillResponseTH removeProductCart(BillResponseTH bill, Long id);
    BillResponseTH updateProductCart(BillResponseTH bill, Long id, Integer updateQty);
    BillResponseTH deleteProductCart(BillResponseTH bill, Long id);
    Bill deleteBill(Long id);
    BillResponseTH create(Employee employee);
    BillResponseTH update(Employee employee, BillResponseTH bill);
    BillResponseTH paymentBill(Employee employee, BillResponseTH bill);


    Bill updateBill(Long id, String address, String addressId, String reciverName,  String phoneNumbe);
    Bill updateShippingFee(Long id, BigDecimal shippingFee);
    Bill updateTypeBill(Bill bill);
    Bill updatePaidAmount(Bill bill);
    Bill updateVoucher(Long billId, Long newVoucherId);
    Bill removeVoucherFromBill(Long billId);
}
