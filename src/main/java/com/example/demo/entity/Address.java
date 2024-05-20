package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "Addresses")

public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Name", columnDefinition = "nvarchar(300)")
    private String name;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "Address", columnDefinition = "nvarchar(300)")
    private String address;

    @Column(name = "AddressId")
    private String addressId;

    @Column(name = "AddressType")
    private String addressType;

    @Column(name = "DefaultAddress")
    private Boolean defaultAddress;

    @ManyToOne
    @JoinColumn(name = "IdCustomer", referencedColumnName = "Id")
    private Customer customer;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "CreatedAt")
    private Date createdAt;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "UpdatedAt")
    private Date updatedAt;

    @Column(name = "Status")
    private int status;

}
