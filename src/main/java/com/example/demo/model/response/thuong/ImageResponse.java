package com.example.demo.model.response.thuong;

import com.example.demo.entity.Color;
import com.example.demo.entity.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "Images")
public class ImageResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Name", columnDefinition = "nvarchar(300)")
    private String name;

    @Column(name = "Path", columnDefinition = "nvarchar(max)")
    private String path;

    @Column(name = "Status")
    private Integer status;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "IdProduct", referencedColumnName = "Id")
    private Product product;


}
