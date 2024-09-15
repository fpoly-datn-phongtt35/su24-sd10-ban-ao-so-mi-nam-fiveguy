package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Voucher;
import com.example.demo.repository.nguyen.NCustomerTypeVoucherRepository;
import com.example.demo.repository.nguyen.NVoucherRepository;
import com.example.demo.repository.nguyen.bill.*;
import com.example.demo.repository.nguyen.product.NProductDetailRepository;
import com.example.demo.service.nguyen.NReturnOrderV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class NReturnOrderV2ServiceImpl implements NReturnOrderV2Service {

    @Autowired
    NReturnOrderRepository returnOrderRepository;

    @Autowired
    NBillDetailRepository billDetailRepository;

    @Autowired
    NBillRepository billRepository;

    @Autowired
    NBillHistoryRepository billHistoryRepository;

    @Autowired
    NProductDetailRepository productDetailRepository;

    @Autowired
    NVoucherRepository voucherRepository;

    @Autowired
    NCustomerTypeVoucherRepository customerTypeVoucherRepository;

    @Autowired
    NPaymentStatusRepository paymentStatusRepository;

    @Transactional(readOnly = true)
    public Voucher findBestVoucher(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        BigDecimal totalAmount = calculateTotalAmountSale(billId);
        List<Voucher> vouchers = voucherRepository
                .findAll(); // Assuming you have a method to fetch all vouchers

        Voucher bestVoucher = null;
        BigDecimal bestDiscount = BigDecimal.ZERO;

        for (Voucher voucher : vouchers) {
            if (isVoucherApplicable(voucher, totalAmount, bill.getCustomer(), bill)) {
                BigDecimal discount = calculateDiscount(voucher, totalAmount);
                if (discount.compareTo(bestDiscount) > 0) {
                    bestDiscount = discount;
                    bestVoucher = voucher;
                }
            }
        }
        return bestVoucher;
    }

    public BigDecimal calculateTotalAmountSale(Long billId) {
        List<BillDetail> billDetails = billDetailRepository.findByBillId(billId);
        BigDecimal totalAmountSale = BigDecimal.ZERO;
        for (BillDetail billDetail : billDetails) {
            BigDecimal detailTotalSale = billDetail.getPromotionalPrice()
                    .multiply(BigDecimal.valueOf(billDetail.getQuantity()));
            totalAmountSale = totalAmountSale.add(detailTotalSale);
        }
        return totalAmountSale;
    }

    private boolean isVoucherApplicable(Voucher voucher, BigDecimal totalAmount,
                                        Customer customer, Bill bill) {
        if (voucher.getStatus() != 1) {
            return false; // Voucher is not active
        }

        if (voucher.getQuantity() <= 0) {
            return false;
        }

        if (voucher.getStartDate().after(new Date()) || voucher.getEndDate().before(new Date())) {
            return false; // Voucher is not within the valid date range
        }

        if (totalAmount.compareTo(BigDecimal.valueOf(voucher.getMinimumTotalAmount())) < 0) {
            return false; // Bill amount is less than the minimum amount required
        }

        // New logic for applyfor
        if (voucher.getApplyfor() == 1) {
            if (customer == null || customer.getCustomerType() == null || customer.getCustomerType().getStatus() != 1 ||
                    !customerTypeVoucherRepository.findAllByVoucherId(voucher.getId()).stream()
                            .anyMatch(ctv -> ctv.getCustomerType()
                                    .equals(customer.getCustomerType()))) {
                return false; // Customer type does not match for applyfor = 1
            }
        }

        if (voucher.getApplyfor() != 0 && voucher.getNumberOfUses() != null && customer != null) {
            long usedCount = billRepository
                    .countByCustomerIdAndVoucherIdAndStatusNotIn(customer.getId(), voucher.getId(),
                            List.of(5, 6, 1, 20, 100));  // Bỏ 1 nếu muốn hiển thị khi voucher chưa xác nhận

            // Kiểm tra nếu voucher đang được sử dụng trong bill hiện tại
            boolean isCurrentBillUsingVoucher =
                    bill.getVoucher() != null && bill.getVoucher().getId().equals(voucher.getId());

            if (usedCount >= voucher.getNumberOfUses() && !isCurrentBillUsingVoucher) {
                return false; // Giới hạn số lần sử dụng voucher đã đạt
            }
        }


        // Check the number of uses limit using repository
//        if (voucher.getNumberOfUses() != null && customer != null) {
//            long usedCount = billRepository
//                    .countByCustomerIdAndVoucherIdAndStatusNotIn(customer.getId(), voucher.getId(),
//                            List.of(5, 6));
//            if (usedCount >= voucher.getNumberOfUses()) {
//                return false; // Voucher usage limit reached
//            }
//        }

        return true; // Voucher is applicable
    }

    private BigDecimal calculateDiscount(Voucher voucher, BigDecimal totalAmount) {
        if (voucher == null || totalAmount == null) {
            return BigDecimal.ZERO;
        }

        if (totalAmount.compareTo(BigDecimal.valueOf(voucher.getMinimumTotalAmount())) < 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal discountValue;
        if (voucher.getDiscountType() == 1) { // Percentage discount
            discountValue = totalAmount.multiply(BigDecimal.valueOf(voucher.getValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

            if (voucher.getMaximumReductionValue() != null ||
                    voucher.getMaximumReductionValue() != 0) {
//                if(discountValue.compareTo(BigDecimal.valueOf(voucher.getMaximumReductionValue())) > 0 ){
//                    return BigDecimal.valueOf(voucher.getMaximumReductionValue());
//                }
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
