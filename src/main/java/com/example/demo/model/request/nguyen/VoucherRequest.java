package com.example.demo.model.request.nguyen;

import com.example.demo.entity.Customer;
import com.example.demo.entity.CustomerType;
import com.example.demo.entity.CustomerVoucher;
import com.example.demo.entity.Voucher;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VoucherRequest {

    private Voucher voucher;

    private List<CustomerType> customerTypeList;

    private List<Customer> customerList;
}
