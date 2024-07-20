package com.example.demo.service.tinh.serviceImpl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.entity.Employee;
import com.example.demo.repository.tinh.BillDetailRepositoryTinh;
import com.example.demo.service.tinh.BillDetailServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

@Service
public class BillDetailServiceImplTinh implements BillDetailServiceTinh {

    @Autowired
    BillDetailRepositoryTinh billDetailRepositoryTinh;

    @Override
    public BillDetail create(BillDetail bill){
        BillDetail bill1 = new BillDetail();
//        Employee employee = new Employee();

        bill1.setBill(bill.getBill());
        bill1.setProductDetail(bill.getProductDetail());
        bill1.setQuantity(bill.getQuantity());
        bill1.setPrice(bill.getPrice());
        bill1.setPromotionalPrice(BigDecimal.valueOf(3));
        bill1.setStatus(1);

        return billDetailRepositoryTinh.save(bill1);
    }


}
