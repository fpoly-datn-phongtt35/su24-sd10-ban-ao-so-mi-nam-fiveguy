package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
//@ToString
@Entity
@Table(name = "BillHistories")
public class BillHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Status")
    private int status;

    @Column(name = "Description", columnDefinition = "nvarchar(300)")
    private String description;

    @Column(name = "Type")
    private int type;

    @Column(name = "Reason")
    private int reason;

    @Column(name = "CreatedAt")
    private Date createdAt;

    @Column(name = "CreatedBy")
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "BillId", referencedColumnName = "Id")
    private Bill bill;
}
