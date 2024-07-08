package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "BillDetails")

public class BillDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Quantity", nullable = false)
    private int quantity;

    @Column(name = "DefectiveProduct")
    private int defectiveProduct;

    @Column(name = "Price", nullable = false)
    private BigDecimal price;

    @Column(name = "PromotionalPrice", nullable = false)
    private BigDecimal promotionalPrice;

    @ManyToOne
    @JoinColumn(name = "IdBill", referencedColumnName = "Id")
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "IdProductDetail", referencedColumnName = "Id")
    private ProductDetail productDetail;

    @Column(name = "Status")
    private int status;
}