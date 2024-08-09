package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Voucher;
import com.example.demo.repository.nguyen.NCustomerTypeVoucherRepository;
import com.example.demo.repository.nguyen.NVoucherRepository;
import com.example.demo.repository.nguyen.bill.NBillDetailRepository;
import com.example.demo.repository.nguyen.bill.NBillRepository;
import com.example.demo.repository.nguyen.product.NProductDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class FindVoucherService {

    private final NBillRepository billRepository;
    private final NBillDetailRepository billDetailRepository;
    private final NVoucherRepository voucherRepository;
    private final NCustomerTypeVoucherRepository customerTypeVoucherRepository;

    @Autowired
    public FindVoucherService(NBillRepository billRepository,
                              NBillDetailRepository billDetailRepository,
                              NVoucherRepository voucherRepository,
                              NCustomerTypeVoucherRepository customerTypeVoucherRepository) {
        this.billRepository = billRepository;
        this.billDetailRepository = billDetailRepository;
        this.voucherRepository = voucherRepository;
        this.customerTypeVoucherRepository = customerTypeVoucherRepository;
    }

    @Transactional(readOnly = true)
    public BestVoucher findBestVoucher(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        BigDecimal totalAmount = calculateTotalAmountSale(billId);
        List<Voucher> vouchers = voucherRepository.findAll();

        BestVoucher bestVoucher = null;
        BigDecimal bestDiscount = BigDecimal.ZERO;

        for (Voucher voucher : vouchers) {
            if (isVoucherApplicable(voucher, totalAmount, bill.getCustomer())) {
                BigDecimal discount = calculateDiscount(voucher, totalAmount);
                if (discount.compareTo(bestDiscount) > 0) {
                    bestDiscount = discount;
                    bestVoucher = new BestVoucher();
                    bestVoucher.setVoucher(voucher);
                    bestVoucher.setDiscoutValue(discount);
                }
            }
        }

        if (bestVoucher != null) {
            bestVoucher = checkAndAdjustVoucher(bestVoucher, totalAmount);
        }

        return bestVoucher;
    }

    public BigDecimal calculateTotalAmountSale(Long billId) {
        List<BillDetail> billDetails = billDetailRepository.findByBillId(billId);
        return billDetails.stream()
                .map(detail -> detail.getPromotionalPrice()
                        .multiply(BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isVoucherApplicable(Voucher voucher, BigDecimal totalAmount,
                                        Customer customer) {
        if (voucher.getStatus() != 1) {
            return false; // Voucher is not active
        }

        LocalDate now = LocalDate.now();
        LocalDate startDate = voucher.getStartDate().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate endDate = voucher.getEndDate().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();

        if (now.isBefore(startDate) || now.isAfter(endDate)) {
            return false; // Voucher is not within the valid date range
        }

        if (totalAmount.compareTo(BigDecimal.valueOf(voucher.getMinimumTotalAmount())) < 0) {
            return false; // Bill amount is less than the minimum amount required
        }

        if (voucher.getApplyfor() == 1 && customer != null) {
            if (customer.getCustomerType() == null ||
                    customerTypeVoucherRepository.findAllByVoucherId(voucher.getId()).stream()
                            .noneMatch(ctv -> ctv.getCustomerType()
                                    .equals(customer.getCustomerType()))) {
                return false; // Customer type does not match
            }
        }

        // Check the number of uses limit using repository
        if (voucher.getNumberOfUses() != null && customer != null) {
            long usedCount = billRepository
                    .countByCustomerIdAndVoucherIdAndStatusNotIn(customer.getId(), voucher.getId(),
                            List.of(5, 6));
            if (usedCount >= voucher.getNumberOfUses()) {
                return false; // Voucher usage limit reached
            }
        }

        return true; // Voucher is applicable
    }

    private BestVoucher checkAndAdjustVoucher(BestVoucher bestVoucher, BigDecimal totalAmount) {
        Voucher voucher = bestVoucher.getVoucher();
        BigDecimal discountValue = bestVoucher.getDiscoutValue();

        // Kiểm tra nếu giá trị giảm giá vượt quá tổng số tiền
        if (discountValue.compareTo(totalAmount) > 0) {
            discountValue = totalAmount;
            bestVoucher.setDiscoutValue(discountValue);
        }

        // Kiểm tra nếu tổng số tiền sau khi áp dụng voucher nhỏ hơn số tiền tối thiểu
        BigDecimal afterDiscountAmount = totalAmount.subtract(discountValue);
        if (afterDiscountAmount.compareTo(BigDecimal.valueOf(voucher.getMinimumTotalAmount())) <
                0) {
            // Tính lại giá trị giảm giá để đảm bảo số tiền tối thiểu
            discountValue = totalAmount
                    .subtract(BigDecimal.valueOf(voucher.getMinimumTotalAmount()));
            bestVoucher.setDiscoutValue(discountValue);
        }

        // Kiểm tra nếu giá trị giảm giá vượt quá giá trị giảm tối đa (nếu có)
        if (voucher.getMaximumReductionValue() != null) {
            BigDecimal maxReduction = BigDecimal.valueOf(voucher.getMaximumReductionValue());
            if (discountValue.compareTo(maxReduction) > 0) {
                bestVoucher.setDiscoutValue(maxReduction);
            }
        }

        return bestVoucher;
    }

    public BigDecimal calculateFinalAmount(Long billId, BestVoucher bestVoucher) {
        BigDecimal totalAmount = calculateTotalAmountSale(billId);
        if (bestVoucher != null) {
            return totalAmount.subtract(bestVoucher.getDiscoutValue());
        }
        return totalAmount;
    }

    public boolean isVoucherStillValid(BestVoucher bestVoucher, BigDecimal currentTotalAmount) {
        if (bestVoucher == null) {
            return false;
        }
        Voucher voucher = bestVoucher.getVoucher();
        return isVoucherApplicable(voucher, currentTotalAmount, null) &&
                currentTotalAmount.compareTo(BigDecimal.valueOf(voucher.getMinimumTotalAmount())) >=
                        0;
    }

    private BigDecimal calculateDiscount(Voucher voucher, BigDecimal totalAmount) {
        if (voucher == null || totalAmount == null ||
                totalAmount.compareTo(BigDecimal.valueOf(voucher.getMinimumTotalAmount())) < 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal discountValue;
        if (voucher.getDiscountType() == 1) { // Percentage discount
            discountValue = totalAmount.multiply(BigDecimal.valueOf(voucher.getValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

            if (voucher.getMaximumReductionValue() != null) {
                return discountValue.min(BigDecimal.valueOf(voucher.getMaximumReductionValue()));
            }
            return discountValue;
        } else if (voucher.getDiscountType() == 2) { // Fixed amount discount
            discountValue = BigDecimal.valueOf(voucher.getValue());
            return discountValue.min(totalAmount); // Ensure discount doesn't exceed total amount
        }

        return BigDecimal.ZERO;
    }
}
