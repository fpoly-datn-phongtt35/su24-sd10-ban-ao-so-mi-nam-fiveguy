package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
//@ToString
@Entity
@Table(name = "Carts")

public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IdCustomer", referencedColumnName = "Id")
    private Customer customer;

//    @Column(name = "CreatedAt", nullable = false)
//    private Date createdAt;
//
//    @Column(name = "UpdatedAt", nullable = false)
//    private Date updatedAt;

    @Column(name = "Status", nullable = false)
    private int status;
}