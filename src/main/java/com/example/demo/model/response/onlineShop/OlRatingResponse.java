package com.example.demo.model.response.onlineShop;

import com.example.demo.entity.Customer;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OlRatingResponse {


    private Long id;

    private String content;

    private int rate;

    private Date createdAt;

    private Date updatedAt;

    private boolean rated;

    private Customer Customer;

    private Long idBillDetail;

    private int status;

    private String reviewer;

    private Date reviewedAt;

    private int approvalStatus;

}