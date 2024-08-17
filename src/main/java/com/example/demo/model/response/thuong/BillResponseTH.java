package com.example.demo.model.response.thuong;

import com.example.demo.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BillResponseTH {

    private Long id;

    private String code;

    private String reciverName;

    private Date deliveryDate;

    private BigDecimal shippingFee;

    private String addressId;

    private String address;

    private String phoneNumber;

    private BigDecimal totalAmount;

    private BigDecimal totalAmountAfterDiscount;

    private BigDecimal paidAmount;

    private BigDecimal paidShippingFee ;

    private Date createdAt;

    private Customer customer;

    private Employee employee;

    private PaymentMethod paymentMethod;

    private Voucher voucher;

    private int typeBill;

    private String note;

    private int status;

    private List<BillDetail> billDetail;

    public BillResponseTH(Long id, String code, String reciverName, Date deliveryDate, BigDecimal shippingFee, String addressId, String address, String phoneNumber, BigDecimal totalAmount, BigDecimal totalAmountAfterDiscount, BigDecimal paidAmount, BigDecimal paidShippingFee, Date createdAt, Customer customer, Employee employee, PaymentMethod paymentMethod, Voucher voucher, int typeBill, String note, int status) {
        this.id = id;
        this.code = code;
        this.reciverName = reciverName;
        this.deliveryDate = deliveryDate;
        this.shippingFee = shippingFee;
        this.addressId = addressId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.totalAmount = totalAmount;
        this.totalAmountAfterDiscount = totalAmountAfterDiscount;
        this.paidAmount = paidAmount;
        this.paidShippingFee = paidShippingFee;
        this.createdAt = createdAt;
        this.customer = customer;
        this.employee = employee;
        this.paymentMethod = paymentMethod;
        this.voucher = voucher;
        this.typeBill = typeBill;
        this.note = note;
        this.status = status;
    }
}
