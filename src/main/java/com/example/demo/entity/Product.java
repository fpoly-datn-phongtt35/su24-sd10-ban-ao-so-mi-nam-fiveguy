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
@ToString
@Entity
@Table(name = "Products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Code")
    private String code;

    @Column(name = "Name", columnDefinition = "nvarchar(300)")
    private String name;

    @Column(name = "Price")
    private BigDecimal price;

    @Column(name = "Describe", columnDefinition = "nvarchar(max)")
    private String describe;

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
    @JoinColumn(name = "IdCategory", referencedColumnName = "Id")
    private Category category;

//    @ManyToOne
//    @JoinColumn(name = "IdBrandSuppiler", referencedColumnName = "Id")
//    private BrandSuppiler brand;

    @ManyToOne
    @JoinColumn(name = "IdSuppiler", referencedColumnName = "Id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "IdMaterial", referencedColumnName = "Id")
    private Material material;

    @ManyToOne
    @JoinColumn(name = "IdWrist", referencedColumnName = "Id")
    private Wrist wrist;

    @ManyToOne
    @JoinColumn(name = "IdCollar", referencedColumnName = "Id")
    private Collar collar;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetail> productDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<ProductSale> productSales;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;


}
