package com.example.demo.entity;

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
    private Double value;

    @Column(name = "NumberOfUses")
    private Integer numberOfUses;

    @Column(name = "DiscountType")
    private String discountType;

    @Column(name = "Describe", columnDefinition = "nvarchar(500)")
    private String describe;

    @Column(name = "StartDate")
    private Date startDate;

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

    @OneToMany(mappedBy = "sale")
    private List<ProductSale> productSales;
}