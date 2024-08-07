package com.example.demo.model.response.thuong;

import com.example.demo.entity.Address;
import com.example.demo.entity.Bill;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CustomerResponseTH {
    private Long id;

    private String code;

    private String fullName;

    private String avatar;

    private Date birthDate;

    private Boolean gender;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;

    private int status;

    private List<Bill> bills;

    private List<Address> addresses;

}
