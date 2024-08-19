package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
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
}
