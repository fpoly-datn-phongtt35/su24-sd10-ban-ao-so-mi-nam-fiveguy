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
//@ToString
@Builder
@Entity
@Table(name = "Sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Code", columnDefinition = "varchar(100)")
    private String code;

    @Column(name = "Name", columnDefinition = "nvarchar(255)")
    private String name;

    @Column(name = "Value")
    private Integer value;

    @Column(name = "NumberOfUses")
    private Integer numberOfUses;

    @Column(name = "DiscountType")
    private Integer discountType;

    @Column(name = "Describe", columnDefinition = "nvarchar(500)")
    private String describe;

    @Column(name = "MaximumDiscountAmount")
    private Integer maximumDiscountAmount;

    @Column(name = "Path", columnDefinition = "nvarchar(max)")
    private String path;

    @Column(name = "StartDate")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EndDate", nullable = false)
    private Date endDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UpdatedAt")
    private Date updatedAt;

    @Column(name = "CreatedBy",columnDefinition = "nvarchar(300)")
    private String createdBy;

    @Column(name = "UpdatedBy",columnDefinition = "nvarchar(300)")
    private String updatedBy;

    @Column(name = "Status")
    private Integer status;

    @JsonIgnore
    @OneToMany(mappedBy = "sale")
    private List<ProductSale> productSales;


//    status
//
//    1 đang hoạt động
//    2 sắp bắt đầu
//    3 hết hạn
//    4 dừng hoạt động
}