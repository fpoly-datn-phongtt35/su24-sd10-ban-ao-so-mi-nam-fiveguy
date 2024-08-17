package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.common.VoucherCommonRepository;
import com.example.demo.repository.onlineShop.OLBillHistoryRepository2;
import com.example.demo.repository.onlineShop.OLBillRepository2;
import com.example.demo.service.onlineShop.OLBillDetailService2;
import com.example.demo.service.onlineShop.OLBillService2;
import com.example.demo.service.onlineShop.OLProductDetailService2;
import com.example.demo.service.onlineShop.OLProductService2;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service

public class OLBillServiceImpl2 implements OLBillService2 {


    @Autowired
    private OLBillDetailService2 olBillDetailService;

    @Autowired
    private OLProductDetailService2 olProductDetailService;

    @Autowired
    private VoucherCommonRepository olVouchersRepository;

    @Autowired
    private OLBillRepository2 olBillRepository;

    @Autowired
    private VoucherCommonRepository voucherRepository;

    @Autowired
    private OLBillHistoryRepository2 billHistoryRepository;


    private boolean isQuantityAvailable(ProductDetail productDetail, int quantityToRemove) {
        int currentQuantity = productDetail.getQuantity() - 1;
        return currentQuantity >= quantityToRemove;
    }

    private void checkProductQuantity(BillDetail billDetail) {
        Optional<ProductDetail> productDetail = olProductDetailService.findById(billDetail.getProductDetail().getId());
        if (productDetail.isPresent()) {

            int quantityToRemove = billDetail.getQuantity();
            if (isQuantityAvailable(productDetail.get(), quantityToRemove)) {
                // Xử lý khi số lượng sản phẩm đủ
            } else if (productDetail.get().getQuantity() == 1) {
                throw new IllegalArgumentException("Sản phẩm: " + productDetail.get().getProduct().getName() + " "
                        + productDetail.get().getProduct().getCategory().getName() + " "
                        + productDetail.get().getProduct().getMaterial().getName() + " " +
                        productDetail.get().getProduct().getCode() + " Màu sắc: "
                        + productDetail.get().getColor().getName() + " Kích cỡ: "
                        + productDetail.get().getSize().getName() + " dã hết hàng");
            } else {
                throw new IllegalArgumentException("Sản phẩm: " + productDetail.get().getProduct().getName() + " "
                        + productDetail.get().getProduct().getCategory().getName() + " "
                        + productDetail.get().getProduct().getMaterial().getName() + " " +
                        productDetail.get().getProduct().getCode() + " Màu sắc: "
                        + productDetail.get().getColor().getName() + " Kích cỡ: "
                        + productDetail.get().getSize().getName() + " Số lượng còn lại: " + (productDetail.get().getQuantity() - 1));
            }
        }
    }

    private String generateCode() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
        String formattedTime = formatter.format(new Date(currentTimeMillis));
        return "HD" + formattedTime;
    }

    @Override
    public ResponseEntity<?> creatBill(JsonNode orderData, Customer customer) {
        if (orderData == null) {
            return ResponseEntity.ok(0);
        }

        ObjectMapper mapper = new ObjectMapper();
        Bill bill = mapper.convertValue(orderData, Bill.class);
        bill.setCreatedAt(new Date());
        bill.setCode(generateCode());
// Kiểm tra số lượng tồn của voucher trước khi sử dụng
        if (bill.getVoucher() != null) {
            Voucher existingVoucher = olVouchersRepository.findById(bill.getVoucher().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));

            if ( existingVoucher.getStatus() == 1 && existingVoucher.getQuantity() > 1) {

            } else {
                return ResponseEntity.ok(3);
            }
        }


        // Kiểm tra và xử lý số lượng sản phẩm trước khi thanh toán
        List<BillDetail> billDetails = mapper.convertValue(orderData.get("billDetail"), new TypeReference<List<BillDetail>>() {
        });
        List<String> insufficientQuantityProducts = new ArrayList<>();

        for (BillDetail detail : billDetails) {
            try {
                checkProductQuantity(detail);
                detail.setBill(bill);
            } catch (IllegalArgumentException e) {
                insufficientQuantityProducts.add(e.getMessage());
            }
        }

        if (!insufficientQuantityProducts.isEmpty()) {
            // Trả về danh sách sản phẩm không đủ số lượng
            return ResponseEntity.ok(Collections.singletonMap(2, insufficientQuantityProducts));
        }

//        bill.setCreatedAt(new Date());
        bill.setCustomer(customer);
        Bill savedBill = olBillRepository.save(bill);
        olBillDetailService.saveAll(billDetails);
        return ResponseEntity.ok(savedBill);
    }


    @Override
    public boolean updatePaymentStatus(Long billId, int paymentStatus) {
        Optional<Bill> optionalBill = olBillRepository.findById(billId);
        if (optionalBill.isPresent()) {
            Bill bill = optionalBill.get();
            bill.setStatus(paymentStatus);
            olBillRepository.save(bill);
            return true;
        }
        return false;
    }


    @Override
    public Bill save(Bill bill) {
        return olBillRepository.save(bill);
    }

    @Override
    public Bill findById(Long id) {
        Optional<Bill> bill = olBillRepository.findById(id);
        if (bill.isPresent()) {
            return bill.get();
        }
        return null;
    }

    @Override
    public Page<Bill> getBillsByCustomerId(Long customerId, String search, Pageable pageable) {
        return olBillRepository.findByCustomer_Id(customerId, search, pageable);
    }

    @Override
    public Page<Bill> getBillsByPhoneNumber(String phoneNumber, String search, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        return olBillRepository.findByPhoneNumber(phoneNumber, search, pageable);
    }


    private void addBillHistoryStatus(Long billId, int status, String description, int type,
                                      int reason,
                                      String createdBy) {
        Bill bill = olBillRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bill ID"));

        BillHistory newHistory = new BillHistory();
        newHistory.setBill(bill);
        newHistory.setStatus(status);
        newHistory.setDescription(description);
        newHistory.setCreatedBy(createdBy);
        newHistory.setType(type);
        newHistory.setReason(reason);
        newHistory.setCreatedAt(new Date());

        billHistoryRepository.save(newHistory);
    }

    @Override
    public Bill updateStatusAndBillStatus(Bill bill, Long id, BillHistory billHistory) {
        Optional<Bill> optionalBill = olBillRepository.findById(id);
        if (!optionalBill.isPresent()) {
            throw new EntityNotFoundException("Bill not found with id " + id);
        }
        if (optionalBill.get().getStatus() == 2 || optionalBill.get().getStatus() == 1) {
            Bill existingBill = optionalBill.get();
            existingBill.setStatus(bill.getStatus());
            existingBill.setReason(bill.getStatus());

            addBillHistoryStatus(existingBill.getId(), billHistory.getStatus(),
                    billHistory.getDescription(), 1, billHistory.getReason(), billHistory.getCreatedBy());
//            List<BillDetail> billDetails = olBillDetailService.findAllByBillIdOrderByIdDesc(id);
//            if (bill.getStatus() == 5 && optionalBill.get().getStatus() == 2) {
//                for (BillDetail billDetail : billDetails) {
//                    ProductDetail productDetail = billDetail.getProductDetail();
//                    int newQuantity = productDetail.getQuantity() + billDetail.getQuantity();
//                    productDetail.setQuantity(newQuantity);
//                    olProductDetailService.save(productDetail);
//                }
//                updateVoucherOnBillCancellation(id);
//            }

            return olBillRepository.save(existingBill);
        }
        return null;
    }

    @Transactional
    public void updateVoucherOnBillCancellation(Long billId) {
        Bill bill = olBillRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));


        Voucher usedVoucher = bill.getVoucher();
        if (usedVoucher != null) {
            // Tăng số lượng voucher lên 1
            usedVoucher.setQuantity(usedVoucher.getQuantity() + 1);
            voucherRepository.save(usedVoucher);

        }


    }
}