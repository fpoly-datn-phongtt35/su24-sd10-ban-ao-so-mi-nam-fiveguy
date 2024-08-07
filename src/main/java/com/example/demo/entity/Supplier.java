//package com.example.demo.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Set;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Setter
//@Getter
//@ToString
//@Builder
//@Entity
//@Table(name = "Suppliers")
//public class Supplier {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "Id")
//    private Long id;
//
//    @Column(name = "Name", columnDefinition = "nvarchar(300)")
//    private String name;
//
//    @Column(name = "Address", columnDefinition = "nvarchar(300)")
//    private String address;
//
//    @Column(name = "PhoneNumber")
//    private String phoneNumber;
//
//    @Column(name = "Email")
//    private String email;
//
//    @Column(name = "CreatedAt")
//    private Date createdAt;
//
//    @Column(name = "UpdatedAt")
//    private Date updatedAt;
//
//    @Column(name = "Status")
//    private Integer status;
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "supplier",cascade = CascadeType.ALL)
//    private Set<Product> products;
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "supplier",cascade = CascadeType.ALL)
//    private Set<BrandSuppiler> brandSuppilers;
//}
