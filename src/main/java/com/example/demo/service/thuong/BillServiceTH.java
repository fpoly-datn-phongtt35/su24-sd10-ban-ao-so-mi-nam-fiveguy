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


    BillResponseTH updateBill(Long id, String address, String addressId, String reciverName,  String phoneNumbe);
    BillResponseTH updateShippingFee(Long id, BigDecimal shippingFee);
    BillResponseTH updateTypeBill(Bill bill);
    BillResponseTH updateVoucher(Long billId, Long newVoucherId);
    BillResponseTH removeVoucherFromBill(Long billId);
}
