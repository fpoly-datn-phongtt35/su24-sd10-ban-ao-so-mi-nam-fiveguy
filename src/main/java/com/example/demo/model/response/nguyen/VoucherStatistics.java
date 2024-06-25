package com.example.demo.model.response.nguyen;

import java.math.BigDecimal;

public class VoucherStatistics {
    private int usageCount;
    private BigDecimal totalRevenue;
    private BigDecimal totalRevenueAfterDiscount;
    private BigDecimal profit;
    private BigDecimal profitMargin;

    public VoucherStatistics(int usageCount, BigDecimal totalRevenue, BigDecimal totalRevenueAfterDiscount, BigDecimal profit, BigDecimal profitMargin) {
        this.usageCount = usageCount;
        this.totalRevenue = totalRevenue;
        this.totalRevenueAfterDiscount = totalRevenueAfterDiscount;
        this.profit = profit;
        this.profitMargin = profitMargin;
    }

    // Getters and setters
    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getTotalRevenueAfterDiscount() {
        return totalRevenueAfterDiscount;
    }

    public void setTotalRevenueAfterDiscount(BigDecimal totalRevenueAfterDiscount) {
        this.totalRevenueAfterDiscount = totalRevenueAfterDiscount;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(BigDecimal profitMargin) {
        this.profitMargin = profitMargin;
    }
}
