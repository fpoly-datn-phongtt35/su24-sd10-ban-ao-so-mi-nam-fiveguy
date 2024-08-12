package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name = "CustomerPointsHistory")
public class CustomerPointsHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CustomerId", nullable = false)
    private Customer customer; // The customer who earned the points

    @ManyToOne
    @JoinColumn(name = "BillId", nullable = true)
    private Bill bill; // The bill associated with the points

    @Column(name = "Points", nullable = false)
    private Integer points; // Number of points earned

    @Column(name = "Note", columnDefinition = "nvarchar(300)")
    private String note;

    @Column(name = "Date", nullable = false)
    private Date date; // Date when the points were earned
}