package com.example.demo.model.response.nguyen;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReturnOrderResponse {

    private Long id;

    private Integer quantity;

    private int type;

    private Bill bill;

    private BillDetail billDetail;

    private String returnReason;

    private Integer returnStatus;

    private Date createdAt;

    private Date updatedAt;

    private String imagePath;
}
