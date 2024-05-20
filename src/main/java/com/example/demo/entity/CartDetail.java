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
@Table(name = "CartDetails")

public class CartDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Quantity")
    private int quantity;

    @Column(name = "Price")
    private BigDecimal price;

    @Column(name = "PromotionalPrice", nullable = false)
    private BigDecimal promotionalPrice;

    @ManyToOne
    @JoinColumn(name = "IdCart", referencedColumnName = "Id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "IdProductDetail", referencedColumnName = "Id")
    private ProductDetail productDetail;

    @Column(name = "Status")
    private int status;
}