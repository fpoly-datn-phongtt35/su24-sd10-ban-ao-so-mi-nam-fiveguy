package com.example.demo.service.point.impl;

import com.example.demo.entity.*;
import com.example.demo.model.response.point.BillWithRevenueDTO;
import com.example.demo.repository.Customer.OlCustomerRepository;
import com.example.demo.repository.onlineShop.OLBillRepository2;
import com.example.demo.repository.point.CustomerPointsHistoryRepository;
import com.example.demo.repository.point.CustomerTypeRepository;
import com.example.demo.repository.point.PointSettingsRepository;
import com.example.demo.service.point.CustomerPointsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerPointsHistoryServiceImpl implements CustomerPointsHistoryService {

    @Autowired
    private  CustomerPointsHistoryRepository repository;

    @Autowired
    private PointSettingsRepository pointSettingsRepository;

    @Autowired
    private OlCustomerRepository olCustomerRepository;

    @Autowired
    private OLBillRepository2 billRepository;

    @Autowired
    private CustomerTypeRepository customerTypeRepository;

    @Override
    public List<CustomerPointsHistory> getPointsHistoryByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public List<CustomerPointsHistory> getPointsHistoryByCustomer(Customer customer) {
        return repository.findByCustomer(customer);
    }

    @Override
    public CustomerPointsHistory savePointsHistory(CustomerPointsHistory customerPointsHistory) {
        return repository.save(customerPointsHistory);
    }

    @Override
    public CustomerPointsHistory updatePointsHistory(CustomerPointsHistory customerPointsHistory) {
        if (customerPointsHistory.getId() == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        return repository.save(customerPointsHistory);
    }

    @Override
    public void addPointsFromBill(Long billId) {
        // Retrieve the totalRevenueAdjusted
        BigDecimal totalRevenueAdjusted = billRepository.findBillWithRevenueById(billId);
        if (totalRevenueAdjusted == null) {
            return;

        }

        // Retrieve the Bill
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (!billOptional.isPresent()) {
            return;

        }
        Bill bill = billOptional.get();
        Customer customer = bill.getCustomer();
        if (customer == null) {
            return;
        }

        // Retrieve the existing CustomerPointsHistory for the bill
        Optional<CustomerPointsHistory> existingPointsHistoryOptional = repository.findByBillId(billId);
        int previousPoints = 0;

        if (existingPointsHistoryOptional.isPresent()) {
            // Subtract the existing points from the customer's total points
            CustomerPointsHistory existingPointsHistory = existingPointsHistoryOptional.get();
            previousPoints = existingPointsHistory.getPoints();
            customer.setPoint(customer.getPoint() - previousPoints);
        }

        // Retrieve the PointSettings
        PointSettings pointSettings = pointSettingsRepository.findFirstByOrderByIdAsc();
        Integer pointsPerAmount = pointSettings.getPointsPerAmount();

        // Calculate the new points based on totalRevenueAdjusted
        Integer newPoints = totalRevenueAdjusted
                .divide(BigDecimal.valueOf(pointsPerAmount), RoundingMode.HALF_UP)
                .intValue();

        if (newPoints < 1){
            throw new RuntimeException("Point < 1");
        }

        // Update the existing points history or create a new one
        CustomerPointsHistory pointsHistory;
        if (existingPointsHistoryOptional.isPresent()) {
            pointsHistory = existingPointsHistoryOptional.get();
            pointsHistory.setPoints(newPoints);
            pointsHistory.setNote("Bạn được cộng: " + newPoints + " điểm sau khi hoàn thành đơn hàng " + bill.getCode());
        } else {
            pointsHistory = CustomerPointsHistory.builder()
                    .customer(customer)
                    .bill(bill)
                    .points(newPoints)
                    .note("Bạn được cộng số điểm: " + newPoints + " sau khi hoàn thành đơn hàng " + bill.getCode())
                    .date(new java.util.Date())
                    .build();
        }

        repository.save(pointsHistory);

        Integer currentPoints = customer.getPoint();
        if (currentPoints == null) {
            currentPoints = 0;
        }
        customer.setPoint(currentPoints + newPoints);

        // Find the appropriate CustomerType with status = 1
        List<CustomerType> customerTypes = customerTypeRepository.findByStatus(1);
        CustomerType appropriateCustomerType = customerTypes.stream()
                .filter(ct -> ct.getMinPoints() <= customer.getPoint() && ct.getMaxPoints() >= customer.getPoint())
                .max((ct1, ct2) -> Integer.compare(ct1.getMaxPoints(), ct2.getMaxPoints()))
                .orElse(null);

        customer.setCustomerType(appropriateCustomerType);
        olCustomerRepository.save(customer);
    }



}