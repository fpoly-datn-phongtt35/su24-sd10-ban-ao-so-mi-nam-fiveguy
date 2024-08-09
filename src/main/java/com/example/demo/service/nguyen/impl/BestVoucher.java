package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.Voucher;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BestVoucher {

    Voucher voucher;

    BigDecimal discoutValue;
}
