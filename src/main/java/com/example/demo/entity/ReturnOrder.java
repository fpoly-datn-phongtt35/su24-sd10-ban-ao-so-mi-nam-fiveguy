package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "ReturnOrders")
public class ReturnOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Quantity")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "IdBill", referencedColumnName = "Id")
    private Bill bill;

    @OneToOne
    @JoinColumn(name = "IdBillDetail", referencedColumnName = "Id")
    private BillDetail billDetail;

    @Column(name = "ReturnReason", columnDefinition = "nvarchar(300)")
    private String returnReason;

    @Column(name = "ReturnStatus")
    private Integer returnStatus;

    @Column(name = "CreatedAt")
    private Date createdAt;

    @Column(name = "UpdatedAt")
    private Date updatedAt;
}
