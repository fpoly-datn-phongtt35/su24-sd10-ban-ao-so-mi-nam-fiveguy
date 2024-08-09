package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
//@ToString
@Entity
@Table(name = "PaymentStatus")
public class PaymentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Code")
    private String code;

    @Column(name = "PaymentAmount ")
    private BigDecimal paymentAmount;

    @Column(name = "Note", columnDefinition = "nvarchar(300)")
    private String note;

    @Column(name = "PaymentDate")
    private Date paymentDate;

//    1 tiền mặt  2 chuyển khoản
    @Column(name = "PaymentMethod")
    private Integer paymentMethod;

//    1 khách hàng thanh toán 2 shipper thanh toán 3 shop hoàn tiền
    @Column(name = "PaymentType")
    private Integer paymentType;

//    1 chưa thanh toán 2 thành công 3 thành công(hoàn) 4 chờ hoàn tiền
    @Column(name = "CustomerPaymentStatus")
    private int customerPaymentStatus;

    @ManyToOne
    @JoinColumn(name = "BillId", referencedColumnName = "Id")
    private Bill bill;


    //    1 đã giao dịch
    //    10 chưa giao dịch thanh toán đơn chưa thanh toán
    //    11 chưa giao dịch thanh toán đơn đã thanh toán thiếu thiền
    //    12 chưa giao dịch hoàn tiền tiền thừa
    //    13 chưa giao dịch hoàn tiền giao thất bại
    //    20 chưa giao dịch hoàn tiền trả hàng
//    @Column(name = "Type")
//    private int type;
}
