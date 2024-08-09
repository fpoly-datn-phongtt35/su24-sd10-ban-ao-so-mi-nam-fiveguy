package com.example.demo.model.response.nguyen;

import com.example.demo.entity.Bill;
import com.example.demo.entity.ProductDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data

public class BillResponse {

    private Long id;

    private String code;

    private int quantity;

    private BigDecimal totalAmount;

    private String nameCustomer;

    private Date createdAt;

    private int typeBill;

    private int status;


    private BigDecimal paidAmount;
}