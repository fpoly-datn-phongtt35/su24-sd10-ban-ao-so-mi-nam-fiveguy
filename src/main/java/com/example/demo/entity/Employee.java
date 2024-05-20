package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
@Getter
@Setter
@Entity
@Table(name="Employees")
public class Employee {

    @Id
    @GeneratedValue( strategy= GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name= "Code")
    private String code;

    @Column(name= "FullName", columnDefinition = "nvarchar(300)")
    private String fullName;

    @Column(name= "Avatar", columnDefinition = "nvarchar(max)")
    private String avatar;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name= "BirthDate")
    private Date birthDate;

    @Column(name= "Gender")
    private Boolean gender;

    @Column(name= "Address", columnDefinition = "nvarchar(300)")
    private String address;

    @ManyToOne
    @JoinColumn(name = "IdAccount", referencedColumnName = "Id")
    private Account account;

    @Column(name= "CreatedAt")
    private Date createdAt;

    @Column(name= "UpdatedAt")
    private Date updatedAt;

    @Column(name= "CreatedBy")
    private String createdBy;

    @Column(name= "UpdatedBy")
    private String updatedBy;

    @Column(name= "Status")
    private Integer status;

}