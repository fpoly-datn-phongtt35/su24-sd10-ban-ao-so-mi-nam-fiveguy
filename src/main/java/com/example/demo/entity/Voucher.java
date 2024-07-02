package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name = "Vouchers")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Code", columnDefinition = "nvarchar(100)")
    private String code;

    @Column(name = "Name", columnDefinition = "nvarchar(255)")
    private String name;

    @Column(name = "Value")
    private Double value;

    @Column(name = "DiscountType")
    private Integer discountType;

    @Column(name = "MaximumReductionValue")
    private Double maximumReductionValue;

    @Column(name = "MinimumTotalAmount")
    private Double minimumTotalAmount;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "NumberOfUses")
    private Integer numberOfUses;

    @Column(name = "Applyfor")
    private Integer applyfor;

    @Column(name = "Describe", columnDefinition = "nvarchar(max)")
    private String describe;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "StartDate")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EndDate")
    private Date endDate;

    @Column(name = "CreatedAt")
    private Date createdAt;

    @Column(name = "UpdatedAt")
    private Date updatedAt;

    @Column(name = "CreatedBy")
    private String createdBy;

    @Column(name = "UpdatedBy")
    private String updatedBy;

    @Column(name = "Status")
    private Integer status;

    @JsonIgnore
    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL)
    private List<Bill> bills;

    @JsonIgnore
    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL)
    private List<CustomerTypeVoucher> customerTypeVouchers;

    @JsonIgnore
    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL)
    private List<CustomerVoucher> customerVouchers;
}