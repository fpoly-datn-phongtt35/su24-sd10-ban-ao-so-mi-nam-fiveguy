package com.example.demo.model.request.thuong;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BillUpdateRequest {
    private String address;
    private String addressId;
    private String reciverName;
    private String phoneNumber;
}