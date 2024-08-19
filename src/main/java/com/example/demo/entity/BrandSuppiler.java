//package com.example.demo.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Setter
//@Getter
//@ToString
//@Builder
//@Entity
//@Table(name = "BrandSuppilers")
//public class BrandSuppiler {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "Id")
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "IdBrand", referencedColumnName = "Id")
//    private Brand brand;
//
//    @ManyToOne
//    @JoinColumn(name = "IdSupplier", referencedColumnName = "Id")
//    private Supplier supplier;
//
//    @Column(name = "Status")
//    private Integer status;
//}
