package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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

    @Column(name = "Status")
    private int status;

    @ManyToOne
    @JoinColumn(name = "IdBill", referencedColumnName = "Id")
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "IdProductDetail", referencedColumnName = "Id")
    private ProductDetail productDetail;

    @JsonIgnore
    @OneToMany(mappedBy = "billDetail",cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<ReturnOrder> returnOrder;

}