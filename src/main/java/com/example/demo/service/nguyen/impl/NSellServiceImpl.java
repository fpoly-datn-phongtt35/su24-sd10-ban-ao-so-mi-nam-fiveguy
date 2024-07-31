package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.nguyen.NCustomerTypeVoucherRepository;
import com.example.demo.repository.nguyen.NVoucherRepository;
import com.example.demo.repository.nguyen.bill.NBillDetailRepository;
import com.example.demo.repository.nguyen.bill.NBillHistoryRepository;
import com.example.demo.repository.nguyen.bill.NBillRepository;
import com.example.demo.repository.nguyen.bill.NPaymentStatusRepository;
import com.example.demo.repository.nguyen.product.NProductDetailRepository;
import com.example.demo.service.nguyen.NSellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NSellServiceImpl implements NSellService {

    @Autowired
    NBillRepository billRepository;

    @Autowired
    NBillHistoryRepository billHistoryRepository;

    @Autowired
    NBillDetailRepository billDetailRepository;

    @Autowired
    NProductDetailRepository productDetailRepository;

    @Autowired
    NVoucherRepository voucherRepository;

    @Autowired
    NCustomerTypeVoucherRepository customerTypeVoucherRepository;

    @Autowired
    NPaymentStatusRepository paymentStatusRepository;

    @Override
    public List<Bill> getAllBillSell() {
        return billRepository.findAllByTypeBillAndStatus(1, 20);
    }

    @Override
    public Bill getById(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        return bill;
    }

    @Override
    public Bill createBillSell(Bill bill, Employee employee) {
        bill.setCode(generateBillCode());
        bill.setTypeBill(1);
        bill.setStatus(20);
        bill.setEmployee(null);

        return billRepository.save(bill);
    }

    private String generateBillCode() {
        String baseCode = "HD";
        String finalCode = baseCode + "00001";

        int counter = 1;
        while (voucherRepository.existsByCode(finalCode)) {
            counter++;
            finalCode = baseCode + String.format("%05d", counter);
        }

        return finalCode;
    }

    @Override
    public void removeBillSell(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        List<BillDetail> billDetails = billDetailRepository.findAllByBillIdOrderByIdDesc(billId);


        billRepository.delete(bill);
    }

    public void updateBillSell(Long billId, Bill billRequest, BillHistory billHistory,
                               PaymentStatus paymentStatus) {

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
    }
}
