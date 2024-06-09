package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name= "Code")
    private String code;

    @Column(name = "FullName", columnDefinition = "nvarchar(300)")
    private String fullName;

    @Column(name = "Avatar")
    private String avatar;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BirthDate")
    private Date birthDate;

    @Column(name = "Gender")
    private Boolean gender;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "CreatedAt")
    private Date createdAt;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "UpdatedAt")
    private Date updatedAt;

    @Column(name = "CreatedBy")
    private String createdBy;

    @Column(name = "UpdatedBy")
    private String updatedBy;

    @Column(name = "Status")
    private int status;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Bill> bills;

    @ManyToOne
    @JoinColumn(name = "IdAccount", referencedColumnName = "Id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "IdCustomerType", referencedColumnName = "Id")
    private CustomerType customerType;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Address> addresses;

}

