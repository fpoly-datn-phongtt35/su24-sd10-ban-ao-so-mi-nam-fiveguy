package com.example.demo.model.response.common;

import lombok.Data;

import java.util.Date;
@Data

public class VoucherDTO {


    private Long id;
    private String code;
    private String name;
    private Double value;
    private Integer discountType;
    private Double maximumReductionValue;
    private Double minimumTotalAmount;
    private Integer quantity;
    private String describe;
    private Date endDate;
    private Integer show;


    public VoucherDTO(Long id, String code, String name, Double value, Integer discountType, Double maximumReductionValue, Double minimumTotalAmount, Integer quantity, String describe, Date endDate, Integer show) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.value = value;
        this.discountType = discountType;
        this.maximumReductionValue = maximumReductionValue;
        this.minimumTotalAmount = minimumTotalAmount;
        this.quantity = quantity;
        this.describe = describe;
        this.endDate = endDate;
        this.show = show;
    }
}
