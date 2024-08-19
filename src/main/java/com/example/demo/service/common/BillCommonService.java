package com.example.demo.service.common;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Customer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface BillCommonService {
    Integer countVoucherUsageByCustomer(Long customerId, Long voucherId);

}
