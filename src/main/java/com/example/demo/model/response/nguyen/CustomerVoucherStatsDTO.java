package com.example.demo.model.response.nguyen;

import java.math.BigDecimal;

public class CustomerVoucherStatsDTO {
    private String customerName;
    private String phoneNumber;
    private String email;
    private Long usageCount;
    private BigDecimal totalPurchaseAmount;
    private BigDecimal totalDiscountAmount;

    // Constructor
    public CustomerVoucherStatsDTO(String customerName, String phoneNumber, String email, Long usageCount, BigDecimal totalPurchaseAmount, BigDecimal totalDiscountAmount) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.usageCount = usageCount;
        this.totalPurchaseAmount = totalPurchaseAmount;
        this.totalDiscountAmount = totalDiscountAmount;
    }

    // Getters and Setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Long usageCount) {
        this.usageCount = usageCount;
    }

    public BigDecimal getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }

    public void setTotalPurchaseAmount(BigDecimal totalPurchaseAmount) {
        this.totalPurchaseAmount = totalPurchaseAmount;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }
}
