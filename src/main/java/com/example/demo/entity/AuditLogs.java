package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
//@ToString
@Entity
@Table(name = "AuditLogs")
public class AuditLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name="Emp_Code")
    private String empCode;

    @Column(name="Implementer", columnDefinition = "nvarchar(300)")
    private String implementer;

    @Column(name="ActionType", columnDefinition = "nvarchar(300)")
    private String actionType;

    @Column(name="Detailed_Action", columnDefinition = "nvarchar(300)")
    private String detailedAction;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @Column(name = "Time")
    private Date time;

    @Column(name="Status")
    private Integer status;
}
