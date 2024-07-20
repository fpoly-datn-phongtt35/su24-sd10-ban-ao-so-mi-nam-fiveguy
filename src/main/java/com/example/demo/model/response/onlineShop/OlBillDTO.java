package com.example.demo.model.response.onlineShop;


import com.example.demo.entity.Employee;
import com.example.demo.entity.PaymentMethod;
import com.example.demo.entity.Voucher;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OlBillDTO {

    private Long id;

    private String code;

    private Date createdAt;

    private Date paymentDate;

    private BigDecimal totalAmount;

    private BigDecimal totalAmountAfterDiscount;

    private String reciverName;

    private Date deliveryDate;

    private BigDecimal shippingFee;

    private String address;

    private String phoneNumber;

    private String note;

    private String reason;

//    private CustomerEntity customerEntity;

    private Employee employee;

    private PaymentMethod paymentMethod;

    private Voucher voucher;

    private int typeBill;

//    private int paymentStatus;

    private int status;

//    private List<OlBillDetailResponse> billDetail;
}