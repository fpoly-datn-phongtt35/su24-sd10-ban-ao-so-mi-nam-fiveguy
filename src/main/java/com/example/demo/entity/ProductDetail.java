package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "ProductDetails")
public class ProductDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "Barcode")
    private String barcode;

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

    @ManyToOne
    @JoinColumn(name = "IdProduct", referencedColumnName = "Id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "IdSize", referencedColumnName = "Id")
    private Size size;

    @ManyToOne
    @JoinColumn(name = "IdColor", referencedColumnName = "Id")
    private Colors color;

    @JsonIgnore
    @OneToMany(mappedBy = "productDetail",cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Image> images;

    @JsonIgnore
    @OneToMany( mappedBy = "productDetail",cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<BillDetail> billDetails;



}
