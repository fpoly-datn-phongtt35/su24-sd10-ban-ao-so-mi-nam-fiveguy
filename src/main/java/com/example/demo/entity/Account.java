package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Accounts")

public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Account")
    private String account;

    @Column(name = "Password")
    private String password;

    @Column(name = "Email")
    private String email;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "ConfirmationCode")
    private String confirmationCode;

    @ManyToOne
    @JoinColumn(name = "IdRole", referencedColumnName = "Id")
    private Role role;

    @Column(name = "Status")
    private Integer status;

  
}
