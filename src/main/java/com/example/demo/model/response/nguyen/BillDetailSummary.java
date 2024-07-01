package com.example.demo.model.response.nguyen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillDetailSummary {

    private int totalQuantity;

    private BigDecimal totalPrice;

    private BigDecimal totalPromotionalPrice;
}
