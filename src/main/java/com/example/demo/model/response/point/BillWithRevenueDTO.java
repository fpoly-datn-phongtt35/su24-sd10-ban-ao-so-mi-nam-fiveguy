package com.example.demo.model.response.point;

import com.example.demo.entity.Bill;
import lombok.Data;


@Data
public class BillWithRevenueDTO {
    private Bill bill;
    private Double totalRevenueAdjusted;

    public BillWithRevenueDTO(Bill bill, Double totalRevenueAdjusted) {
        this.bill = bill;
        this.totalRevenueAdjusted = totalRevenueAdjusted;
    }

}