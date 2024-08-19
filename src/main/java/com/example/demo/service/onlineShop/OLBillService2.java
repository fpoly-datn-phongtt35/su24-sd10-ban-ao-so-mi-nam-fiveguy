package com.example.demo.service.onlineShop;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillHistory;
import com.example.demo.entity.Customer;
import com.example.demo.model.response.onlineShop.OlBillDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface OLBillService2 {

    ResponseEntity<?> creatBill(@RequestBody JsonNode orderData, Customer customer);

//    Page<Bill> findLatestBillsByCustomerId(Long customerId, int page, int size);

    boolean updatePaymentStatus(Long billId, int paymentStatus);

//    OlBillDTO findBYId(Long id);

    Bill save(Bill bill);

    Bill findById(Long id);
//
//    List<Bill> findByPhoneNumber(String pn);

//    order
    Page<Bill> getBillsByCustomerId(Long customerId, String search, Pageable pageable);

    Page<Bill> getBillsByPhoneNumber(String phoneNumber, String search, int page, int size);

    Bill updateStatusAndBillStatus(Bill bill, Long id, BillHistory billHistory);
}
