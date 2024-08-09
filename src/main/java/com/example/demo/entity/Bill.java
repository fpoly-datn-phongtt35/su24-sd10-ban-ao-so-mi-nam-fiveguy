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
@Entity
@Table(name = "Bills")

public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Code")
    private String code;

//        @Column(name = "CreatedAt")
//    private Date createdAt;

//    @Column(name = "PaymentDate")
//    private Date paymentDate;

    @Column(name = "ReciverName", columnDefinition = "nvarchar(300)")
    private String reciverName;

    @Column(name = "DeliveryDate")
    private Date deliveryDate;

    @Column(name = "ShippingFee")
    private BigDecimal shippingFee;

    @Column(name = "TransId")
    private String transId;

    @Column(name = "AddressId")
    private String addressId;

    @Column(name = "Address", columnDefinition = "nvarchar(300)")
    private String address;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "TotalAmount")
    private BigDecimal totalAmount;

    @Column(name = "TotalAmountAfterDiscount")
    private BigDecimal totalAmountAfterDiscount;

    @Column(name = "PaidAmount")
    private BigDecimal paidAmount;

    @Column(name = "PaidShippingFee ")
    private BigDecimal paidShippingFee ;

    @Column(name = "CreatedAt")
    private Date createdAt;

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

    @Column(name = "TypeBill")
    private int typeBill;

    @Column(name = "Reason")
    private int reason;

    @Column(name = "Note", columnDefinition = "nvarchar(300)")
    private String note;

    @Column(name = "Status")
    private int status;

    @JsonIgnore
    @OneToMany(mappedBy = "bill",  cascade = CascadeType.ALL)
    private List<BillDetail> billDetail;

    @JsonIgnore
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<BillHistory> billHistories;

    @JsonIgnore
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<PaymentStatus> paymentStatuses;

    @JsonIgnore
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<ReturnOrder> returnOrders;
}