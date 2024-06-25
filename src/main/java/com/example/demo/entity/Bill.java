package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "Bills")

public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Code")
    private String code;

        @Column(name = "CreatedAt")
    private Date createdAt;

    @Column(name = "PaymentDate")
    private Date paymentDate;

    @Column(name = "TotalAmount")
    private BigDecimal totalAmount;

    @Column(name = "TotalAmountAfterDiscount")
    private BigDecimal totalAmountAfterDiscount;

    @Column(name = "ReciverName", columnDefinition = "nvarchar(300)")
    private String reciverName;

    @Column(name = "DeliveryDate")
    private Date deliveryDate;

    @Column(name = "ShippingFee")
    private BigDecimal shippingFee;

    @Column(name = "Address", columnDefinition = "nvarchar(300)")
    private String address;

    @Column(name = "TransId")
    private String transId;  // Mã giao dịch Momo dùng để hoàn tiền

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "Note", columnDefinition = "nvarchar(300)")
    private String note;

    @Column(name = "Reason")
    private String reason;

    @ManyToOne
    @JoinColumn(name = "IdCustomer", referencedColumnName = "Id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "IdEmployee", referencedColumnName = "Id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "IdPaymentMethod", referencedColumnName = "Id")
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "IdVoucher", referencedColumnName = "Id")
    private Voucher voucher;

    @Column(name = "typeBill")
    private int typeBill;


    @Column(name = "Status")
    private int status;

    @JsonIgnore
    @OneToMany(mappedBy = "bill",  cascade = CascadeType.ALL)
    private List<BillDetail> billDetail;
}