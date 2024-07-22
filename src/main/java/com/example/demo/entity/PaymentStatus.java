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

    @Column(name = "CustomerPaymentStatus")
    private int customerPaymentStatus;

    @ManyToOne
    @JoinColumn(name = "BillId", referencedColumnName = "Id")
    private Bill bill;
}
