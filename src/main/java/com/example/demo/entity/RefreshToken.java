package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "RefreshTokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Token")
    private String token;

    @Column(name = "ExpiryDate")
    private Date expiryDate;

    @ManyToOne
    @JoinColumn(name = "IdAccount", referencedColumnName = "Id")
    private Account account;


}
