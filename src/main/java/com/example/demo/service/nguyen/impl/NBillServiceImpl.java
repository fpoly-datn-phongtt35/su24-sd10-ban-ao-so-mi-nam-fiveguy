package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillHistory;
import com.example.demo.repository.nguyen.NBillHistoryRepository;
import com.example.demo.repository.nguyen.NBillRepository;
import com.example.demo.repository.nguyen.BillSpecification;
import com.example.demo.service.nguyen.NBillService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NBillServiceImpl implements NBillService {

    @Autowired
    private NBillRepository billRepository;

    @Autowired
    NBillHistoryRepository billHistoryRepository;

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

    @Override
    public Bill updateStatusAndBillStatus(Bill bill, Long id, BillHistory billHistory) {
        Optional<Bill> optionalBill = billRepository.findById(id);
        if (!optionalBill.isPresent()) {
            throw new EntityNotFoundException("Bill not found with id " + id);
        }

        Bill existingBill = optionalBill.get();
        existingBill.setStatus(bill.getStatus());

        addBillHistoryStatus(existingBill.getId(), billHistory.getStatus(),
                billHistory.getDescription(), "Admin");

        return null;
    }

    @Override
    public Bill updateShipmentDetail(Bill bill, Long id) {
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
        newHistory.setCreatedBy("Admin");
        newHistory.setCreatedAt(new Date());

        billHistoryRepository.save(newHistory);

        return billRepository.save(existingBill);
    }

    private void addBillHistoryStatus(Long billId, int status, String description, String createdBy) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bill ID"));

        List<BillHistory> histories = billHistoryRepository.findByBillIdOrderByCreatedAtAsc(billId);

        int nextStatus = 1; // Trạng thái mặc định là 1 - Chờ xác nhận nếu chưa có lịch sử nào

        if (!histories.isEmpty()) {
            BillHistory lastHistory = histories.get(histories.size() - 1);
            int lastStatus = lastHistory.getStatus();

            if (status == 6 && lastStatus >= 1 && lastStatus <= 4) {
                nextStatus = 6; // Trạng thái "Đã hủy" có thể thêm từ trạng thái 1 đến 4
            } else if (lastStatus == 1 && status == 2) {
                nextStatus = 2; // Đã xác nhận
            } else if (lastStatus == 2 && status == 3) {
                nextStatus = 3; // Chờ giao hàng
            } else if (lastStatus == 3 && status == 4) {
                nextStatus = 4; // Đang giao hàng
            } else if (lastStatus == 4 && status == 5) {
                nextStatus = 5; // Thành công
            } else {
                throw new IllegalStateException("Invalid status transition");
            }
        }

        BillHistory newHistory = new BillHistory();
        newHistory.setBill(bill);
        newHistory.setStatus(nextStatus);
        newHistory.setDescription(description);
        newHistory.setCreatedBy(createdBy);
        newHistory.setCreatedAt(new Date());

        billHistoryRepository.save(newHistory);
    }

}
