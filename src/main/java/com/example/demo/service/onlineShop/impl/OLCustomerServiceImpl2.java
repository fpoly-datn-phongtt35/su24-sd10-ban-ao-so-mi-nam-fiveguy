package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.Customer;
import com.example.demo.entity.CustomerType;
import com.example.demo.repository.onlineShop.OLCustomerRepository2;
import com.example.demo.service.onlineShop.OLCustomerService2;
import com.example.demo.service.onlineShop.OLCustomerTypeService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class OLCustomerServiceImpl2 implements OLCustomerService2 {

    @Autowired
    private OLCustomerRepository2 olCustomerRepository2;

    @Autowired
    private OLCustomerTypeService2 olCustomerTypeService2;

    private static final BigDecimal POINT_CONVERSION_RATE = new BigDecimal(10000);

    @Override
    public int convertAmountToPoints(BigDecimal totalAmount) {
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }
        return totalAmount.divide(POINT_CONVERSION_RATE).intValue();
    }

    @Override
    public void updateCustomerPoints(Long customerId, Integer points) {
        Optional<Customer> customerOptional = olCustomerRepository2.findById(customerId);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            int updatedPoints = (customer.getPoint() != null ? customer.getPoint() : 0) + points;
            customer.setPoint(updatedPoints);

            // Xác định loại khách hàng mới dựa trên điểm tích lũy
            CustomerType newCustomerType = determineCustomerType(updatedPoints);
            if (newCustomerType != null) {
                customer.setCustomerType(newCustomerType);
            }

            olCustomerRepository2.save(customer);
        } else {
            throw new RuntimeException("Customer not found with id: " + customerId);
        }
    }

    private CustomerType determineCustomerType(int points) {
        if (points >= 1000) { // Kim Cương
            return olCustomerTypeService2.getCustomerTypeByCode(5).orElse(null);
        } else if (points >= 500) { // Bạch Kim
            return olCustomerTypeService2.getCustomerTypeByCode(4).orElse(null);
        } else if (points >= 200) { // Vàng
            return olCustomerTypeService2.getCustomerTypeByCode(3).orElse(null);
        } else if (points > 100) { // Bạc
            return olCustomerTypeService2.getCustomerTypeByCode(2).orElse(null);
        } else {  // Mới
            return olCustomerTypeService2.getCustomerTypeByCode(1).orElse(null);

        }
    }

}
