package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name = "VoucherDetails")
public class VoucherDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IdVoucher", referencedColumnName = "Id")
    private Voucher voucher;

    @ManyToOne
    @JoinColumn(name = "IdCustomerType", referencedColumnName = "Id")
    private CustomerType customerType;

    @ManyToOne
    @JoinColumn(name = "IdPaymentMethod", referencedColumnName = "Id")
    private PaymentMethod paymentMethod;

    @Column(name = "CreatedAt")
    private Date createdAt;

    @Column(name = "UpdatedAt")
    private Date updatedAt;

    @Column(name = "Status")
    private Integer status;
}