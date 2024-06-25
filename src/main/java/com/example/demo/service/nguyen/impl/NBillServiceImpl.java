package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.Bill;
import com.example.demo.repository.nguyen.NBillRepository;
import com.example.demo.repository.nguyen.BillSpecification;
import com.example.demo.service.nguyen.NBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NBillServiceImpl implements NBillService {

    @Autowired
    private NBillRepository NBillRepository;

    @Override
    public List<Bill> getAll() {
        return NBillRepository.findAll();
    }

    @Override
    public Bill getById(Long id) {
        return NBillRepository.findById(id).get();
    }

    @Override
    public List<Bill> searchBills(String code, String customerName, String phoneNumber,
                                  Integer typeBill, Date createdAt, Integer status) {
        return NBillRepository
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

        return NBillRepository.findAll(spec, pageable);
    }
}
