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
@Table(name = "Productsales")
public class ProductSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "PromotionalPrice")
    private Double promotionalPrice;

    @Column(name = "DiscountPrice")
    private Double discountPrice;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "CreatedAt")
    private Date createdAt;

    @Column(name = "CreatedBy")
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "IdSale", referencedColumnName = "Id")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "IdProduct", referencedColumnName = "Id")
    private Product product;
}