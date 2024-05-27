package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private Integer promotionalPrice;

    @Column(name = "DiscountPrice")
    private Integer discountPrice;

    @Column(name = "Status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "IdSale", referencedColumnName = "Id")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "IdProduct", referencedColumnName = "Id")
    private Product product;
}