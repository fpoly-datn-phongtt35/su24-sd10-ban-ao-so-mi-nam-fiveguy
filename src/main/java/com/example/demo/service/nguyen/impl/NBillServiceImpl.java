package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.entity.BillHistory;
import com.example.demo.entity.ProductDetail;
import com.example.demo.repository.nguyen.bill.NBillDetailRepository;
import com.example.demo.repository.nguyen.bill.NBillHistoryRepository;
import com.example.demo.repository.nguyen.bill.NBillRepository;
import com.example.demo.repository.nguyen.bill.BillSpecification;
import com.example.demo.repository.nguyen.product.NProductDetailRepository;
import com.example.demo.service.nguyen.NBillService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NBillServiceImpl implements NBillService {

    @Autowired
    private NBillRepository billRepository;

    @Autowired
    NBillHistoryRepository billHistoryRepository;

    @Autowired
    NBillDetailRepository billDetailRepository;

    @Autowired
    NProductDetailRepository productDetailRepository;


    @Override
    public List<Bill> getAll() {
        return billRepository.findAll();
    }

    @Override
    public Bill getById(Long id) {
        return billRepository.findById(id).get();
    }

    @Override
    public List<Bill> searchBills(String code, String customerName, String phoneNumber,
                                  Integer typeBill, Date createdAt, Integer status) {
        return billRepository
                .findByFilter(code, customerName, phoneNumber, typeBill, createdAt, status);
    }

    @Override
    public Page<Bill> searchBills(String code, String customerName, String phoneNumber,
                                  Integer typeBill, Date startDate, Date endDate, Integer status,
                                  Pageable pageable) {
        Specification<Bill> spec = Specification.where(null);

        if (code != null && !code.isEmpty()) {
            spec = spec.and(BillSpecification.hasCode(code));
        }
        if (customerName != null && !customerName.isEmpty()) {
            spec = spec.and(BillSpecification.hasCustomerName(customerName));
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            spec = spec.and(BillSpecification.hasPhoneNumber(phoneNumber));
        }
        if (typeBill != null) {
            spec = spec.and(BillSpecification.hasTypeBill(typeBill));
        }
        if (startDate != null && endDate != null) {
            spec = spec.and(BillSpecification.createdAtBetween(startDate, endDate));
        }
        if (status != null) {
            spec = spec.and(BillSpecification.hasStatus(status));
        }

        return billRepository.findAll(spec, pageable);
    }

    public Bill updateStatusAndBillStatus1(Bill bill, Long id, BillHistory billHistory) {
        Optional<Bill> optionalBill = billRepository.findById(id);
        if (!optionalBill.isPresent()) {
            throw new EntityNotFoundException("Bill not found with id " + id);
        }

        Bill existingBill = optionalBill.get();
        existingBill.setStatus(bill.getStatus());

        addBillHistoryStatus(existingBill.getId(), billHistory.getStatus(),
                billHistory.getDescription(), 1, billHistory.getReason(), "Admin");

        return billRepository.save(existingBill);
    }

    @Override
    public Bill updateStatusAndBillStatus(Bill bill, Long id, BillHistory billHistory) {
        Optional<Bill> optionalBill = billRepository.findById(id);
        if (!optionalBill.isPresent()) {
            throw new EntityNotFoundException("Bill not found with id " + id);
        }

        if (isQuantityExceedsProductDetail(id)) {
            return null;
        }

        Bill existingBill = optionalBill.get();
        existingBill.setReason(bill.getReason());
        existingBill.setStatus(bill.getStatus());
        existingBill.setCustomer(null);
        System.out.println("aaaaaa" + existingBill);

        addBillHistoryStatus(existingBill.getId(), billHistory.getStatus(),
                billHistory.getDescription(), 1, billHistory.getReason(),
                billHistory.getCreatedBy());

        updateQuantityProductDetail(existingBill.getId(), existingBill.getStatus());

//        Bill returnBill = billRepository.save(existingBill);
        return null;
    }

    @Override
    public Bill updateShipmentDetail(Bill bill, Long id, String fullName) {
        Optional<Bill> optionalBill = billRepository.findById(id);
        if (!optionalBill.isPresent()) {
            throw new EntityNotFoundException("Bill not found with id " + id);
        }

        Bill existingBill = optionalBill.get();
        existingBill.setReciverName(bill.getReciverName());
        existingBill.setPhoneNumber(bill.getPhoneNumber());
        existingBill.setAddress(bill.getAddress());

        BillHistory newHistory = new BillHistory();
        newHistory.setBill(existingBill);
        newHistory.setStatus(existingBill.getStatus());
        newHistory.setDescription("Cập nhật thông tin giao hàng");
        newHistory.setCreatedBy(fullName);
        newHistory.setType(2);
        newHistory.setReason(0);
        newHistory.setCreatedAt(new Date());

        billHistoryRepository.save(newHistory);

        return billRepository.save(existingBill);
    }

    private void addBillHistoryStatus(Long billId, int status, String description, int type,
                                      int reason,
                                      String createdBy) {
        Bill bill = billRepository.findById(billId)
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

    @Transactional
    public void updateQuantityProductDetail1(Long billId, int newStatus) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found"));

        if (newStatus == 2 && bill.getStatus() != 2) {
            List<BillDetail> billDetails = billDetailRepository
                    .findAllByBillIdOrderByIdDesc(billId);

            for (BillDetail billDetail : billDetails) {
                ProductDetail productDetail = billDetail.getProductDetail();
                int newQuantity = productDetail.getQuantity() - billDetail.getQuantity();
                if (newQuantity < 0) {
                    throw new IllegalArgumentException("Not enough product quantity");
                }
                productDetail.setQuantity(newQuantity);
                productDetailRepository.save(productDetail);
            }
        }

//        bill.setStatus(newStatus);
//        billRepository.save(bill);
    }

    @Transactional
    public void updateQuantityProductDetail(Long billId, int newStatus) {
        System.out.println(billId + " " + newStatus);
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found"));
        System.out.println("idbill" + bill.getId());
        List<BillDetail> billDetails = billDetailRepository.findAllByBillIdOrderByIdDesc(billId);
        if (newStatus == 2) {

            for (BillDetail billDetail : billDetails) {
                ProductDetail productDetail = billDetail.getProductDetail();
                int newQuantity = productDetail.getQuantity() - billDetail.getQuantity();
                if (newQuantity < 0) {
                    throw new IllegalArgumentException("Not enough product quantity");
                }
                productDetail.setQuantity(newQuantity);
                productDetailRepository.save(productDetail);
            }
        } else if (newStatus == 5 || newStatus == 6) {
            System.out.println("aa");
            for (BillDetail billDetail : billDetails) {
                ProductDetail productDetail = billDetail.getProductDetail();
                int newQuantity = productDetail.getQuantity() + billDetail.getQuantity();
                productDetail.setQuantity(newQuantity);
                productDetailRepository.save(productDetail);
            }
        }

        bill.setStatus(newStatus);
        billRepository.save(bill);
    }

    @Transactional
    public boolean isQuantityExceedsProductDetail(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Bill not found with id " + billId));

        List<BillDetail> billDetails = bill.getBillDetail();
        for (BillDetail billDetail : billDetails) {
            ProductDetail productDetail = billDetail.getProductDetail();
            if (billDetail.getQuantity() > productDetail.getQuantity()) {
                return true;
            }
        }
        return false;
    }
}
