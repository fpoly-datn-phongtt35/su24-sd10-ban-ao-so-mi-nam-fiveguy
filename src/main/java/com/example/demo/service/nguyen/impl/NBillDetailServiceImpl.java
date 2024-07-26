package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.*;
import com.example.demo.model.response.nguyen.BillDetailSummary;
import com.example.demo.repository.nguyen.NCustomerTypeVoucherRepository;
import com.example.demo.repository.nguyen.NVoucherRepository;
import com.example.demo.repository.nguyen.bill.NBillDetailRepository;
import com.example.demo.repository.nguyen.bill.NBillRepository;
import com.example.demo.repository.nguyen.product.NProductDetailRepository;
import com.example.demo.service.nguyen.NBillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class NBillDetailServiceImpl implements NBillDetailService {

    @Autowired
    NBillDetailRepository billDetailRepository;

    @Autowired
    NBillRepository billRepository;

    @Autowired
    NProductDetailRepository productDetailRepository;

    @Autowired
    NVoucherRepository voucherRepository;

    @Autowired
    NCustomerTypeVoucherRepository customerTypeVoucherRepository;


    @Override
    public BillDetail getById(Long id) {
        return billDetailRepository.findById(id).get();
    }

    @Override
    public List<BillDetail> getAllByBillId(Long billId) {
        return billDetailRepository.findAllByBillIdOrderByIdDesc(billId);
    }

    @Override
    public BillDetailSummary getBillDetailSummaryByBillId(Long billId) {
        List<BillDetail> billDetails = billDetailRepository.findByBillId(billId);

        int totalQuantity = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalPromotionalPrice = BigDecimal.ZERO;

        for (BillDetail bd : billDetails) {
            totalQuantity += bd.getQuantity();

            BigDecimal pricePerItem = bd.getPrice();
            BigDecimal totalPriceForItem = pricePerItem
                    .multiply(BigDecimal.valueOf(bd.getQuantity()));
            totalPrice = totalPrice.add(totalPriceForItem);

            BigDecimal promotionalPricePerItem = bd.getPromotionalPrice();
            BigDecimal totalPromotionalPriceForItem = promotionalPricePerItem
                    .multiply(BigDecimal.valueOf(bd.getQuantity()));
            totalPromotionalPrice = totalPromotionalPrice.add(totalPromotionalPriceForItem);
        }

        return new BillDetailSummary(totalQuantity, totalPrice, totalPromotionalPrice);
    }

    public BillDetailSummary getBillDetailSummaryByBillId1(Long billId) {
        List<BillDetail> billDetails = billDetailRepository.findByBillId(billId);

        int totalQuantity = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalPromotionalPrice = BigDecimal.ZERO;

        for (BillDetail bd : billDetails) {
            totalQuantity += bd.getQuantity();

            BigDecimal pricePerItem = bd.getPromotionalPrice().compareTo(BigDecimal.ZERO) == 0
                    ? bd.getPrice()
                    : bd.getPromotionalPrice();

            BigDecimal totalPriceForItem = pricePerItem
                    .multiply(BigDecimal.valueOf(bd.getQuantity()));
            totalPrice = totalPrice.add(totalPriceForItem);

            if (bd.getPromotionalPrice().compareTo(BigDecimal.ZERO) != 0) {
                totalPromotionalPrice = totalPromotionalPrice.add(totalPriceForItem);
            }
        }

        return new BillDetailSummary(totalQuantity, totalPrice, totalPromotionalPrice);
    }

    @Transactional
    public BillDetail addProductDetailToBill(Long billId, Long productDetailId, int quantity,
                                             BigDecimal price, BigDecimal promotionalPrice) {
        // Fetch the bill
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billId));

        // Fetch the product detail
        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new RuntimeException(
                        "Product detail not found with id: " + productDetailId));

        // Check if the product detail already exists in the bill
        Optional<BillDetail> existingBillDetailOptional = billDetailRepository
                .findByBillAndProductDetail(bill, productDetail);
        if (existingBillDetailOptional.isPresent()) {
            // Update existing BillDetail quantity and price
            BillDetail existingBillDetail = existingBillDetailOptional.get();
            existingBillDetail.setQuantity(existingBillDetail.getQuantity() + quantity);
            existingBillDetail.setPrice(price);
            existingBillDetail.setPromotionalPrice(promotionalPrice);

            // Save the updated BillDetail
            existingBillDetail = billDetailRepository.save(existingBillDetail);

            // Update the Bill's total amount
//            BigDecimal totalPriceIncrement = price.multiply(BigDecimal.valueOf(quantity));
//            bill.setTotalAmount(bill.getTotalAmount().add(totalPriceIncrement));
//            Bill returnBill = billRepository.save(bill);

            autoSetVoucher(bill.getId());
            // Update the ProductDetail quantity
//            productDetail.setQuantity(productDetail.getQuantity() - quantity);
//            productDetailRepository.save(productDetail);

            return existingBillDetail;
        } else {
            // Create a new BillDetail instance
            BillDetail billDetail = new BillDetail();
            billDetail.setBill(bill);
            billDetail.setProductDetail(productDetail);
            billDetail.setQuantity(quantity);
            billDetail.setPrice(price);
            billDetail.setPromotionalPrice(promotionalPrice);
            billDetail.setDefectiveProduct(0);
            billDetail.setStatus(1);

            // Save the BillDetail instance
            billDetail = billDetailRepository.save(billDetail);

            // Update the Bill's total amount
//            BigDecimal totalPriceIncrement = price.multiply(BigDecimal.valueOf(quantity));
//            bill.setTotalAmount(bill.getTotalAmount().add(totalPriceIncrement));
//            Bill returnBill = billRepository.save(bill);

            autoSetVoucher(bill.getId());

            // Update the ProductDetail quantity
//            productDetail.setQuantity(productDetail.getQuantity() - quantity);
//            productDetailRepository.save(productDetail);

            return billDetail;
        }
    }

    @Transactional
    public void removeProductDetailFromBill(Long billDetailId) {
        // Fetch the BillDetail
        BillDetail billDetail = billDetailRepository.findById(billDetailId)
                .orElseThrow(() -> new RuntimeException(
                        "BillDetail not found with id: " + billDetailId));

        // Fetch the associated Bill
        Bill bill = billDetail.getBill();

        // Subtract the BillDetail amount from the Bill's total amount
//        BigDecimal amountToSubtract = billDetail.getPrice()
//                .multiply(BigDecimal.valueOf(billDetail.getQuantity()));
//        BigDecimal amountToSubtract = calculateTotalAmountSale(bill.getId());
//        bill.setTotalAmount(bill.getTotalAmount().subtract(amountToSubtract));

        // Update the ProductDetail quantity
        ProductDetail productDetail = billDetail.getProductDetail();
//        productDetail.setQuantity(productDetail.getQuantity() + billDetail.getQuantity());
//        productDetailRepository.save(productDetail);

        // Save the updated Bill
//        Bill returnBill = billRepository.save(bill);


        // Delete the BillDetail
        billDetailRepository.delete(billDetail);

        autoSetVoucher(bill.getId());
    }

    @Transactional
    public BillDetail updateBillDetailQuantity(Long billDetailId, int newQuantity) {
        // Fetch the BillDetail
        BillDetail billDetail = billDetailRepository.findById(billDetailId)
                .orElseThrow(() -> new RuntimeException(
                        "BillDetail not found with id: " + billDetailId));

        // Fetch the associated Bill and ProductDetail
        Bill bill = billDetail.getBill();
        ProductDetail productDetail = billDetail.getProductDetail();

        // Calculate the quantity difference
        int quantityDifference = newQuantity - billDetail.getQuantity();

        // Update the BillDetail quantity
        billDetail.setQuantity(newQuantity);

        // Update the ProductDetail quantity
//        productDetail.setQuantity(productDetail.getQuantity() - quantityDifference);
//        productDetailRepository.save(productDetail);

        // Update the Bill's total amount
//        BigDecimal amountDifference = billDetail.getPrice()
//                .multiply(BigDecimal.valueOf(quantityDifference));
//        bill.setTotalAmount(bill.getTotalAmount().add(amountDifference));
//        Bill returnBill = billRepository.save(bill);

        BillDetail returnBill = billDetailRepository.save(billDetail);

        autoSetVoucher(bill.getId());

        // Save the updated BillDetail
        return returnBill;
    }

    @Transactional
    public void autoSetVoucher(Long billId) {
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (billOptional.isEmpty()) return;

        Bill bill = billOptional.get();
        BigDecimal totalAmountSale = calculateTotalAmountSale(bill.getId());
        BigDecimal currentTotalAmount = bill.getTotalAmount();
        Voucher currentVoucher = bill.getVoucher();

        bill.setTotalAmount(totalAmountSale);
        // Tìm voucher tốt nhất cho tổng tiền mới
        Voucher bestVoucher = findBestVoucher(bill.getId());

        System.out.println(billDetailRepository.findByBillId(billId).size());

        // Tính toán giá trị giảm giá
        BigDecimal currentDiscountValue = (currentVoucher != null) ? calculateDiscount(currentVoucher,
                bill.getTotalAmount()) : BigDecimal.ZERO;;
        BigDecimal bestDiscountValue = (bestVoucher != null) ? calculateDiscount(bestVoucher,
                bill.getTotalAmount()) : BigDecimal.ZERO;


        System.out.println("1");

        // Quyết định voucher nào sẽ được áp dụng
        Voucher voucherToApply;
        BigDecimal discountToApply;

        if (bestVoucher == null ||
                !isVoucherApplicable(bestVoucher, totalAmountSale, bill.getCustomer())) {
            voucherToApply = null;
            discountToApply = BigDecimal.ZERO;
        } else if (currentVoucher == null ||
                !isVoucherApplicable(currentVoucher, totalAmountSale, bill.getCustomer()) ||
                bestDiscountValue.compareTo(currentDiscountValue) > 0) {
            voucherToApply = bestVoucher;
            discountToApply = bestDiscountValue;
        } else {
            voucherToApply = currentVoucher;
            discountToApply = currentDiscountValue;
        }

        System.out.println("aaa1");

        // Cập nhật bill
//        bill.setTotalAmount(totalAmountSale);
        bill.setTotalAmountAfterDiscount(totalAmountSale.subtract(discountToApply));
        bill.setVoucher(voucherToApply);

        // Lưu bill
        billRepository.save(bill);

        // Cập nhật số lượng voucher nếu có thay đổi
        if (!Objects.equals(voucherToApply, currentVoucher)) {
            updateVoucherQuantities(currentVoucher, voucherToApply);
        }
    }

    private void updateVoucherQuantities(Voucher oldVoucher, Voucher newVoucher) {
        if (oldVoucher != null) {
            oldVoucher.setQuantity(oldVoucher.getQuantity() + 1);
            voucherRepository.save(oldVoucher);
        }
        if (newVoucher != null) {
            newVoucher.setQuantity(newVoucher.getQuantity() - 1);
            voucherRepository.save(newVoucher);
        }
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
            if (isVoucherApplicable(voucher, totalAmount, bill.getCustomer())) {
                BigDecimal discount = calculateDiscount(voucher, totalAmount);
                if (discount.compareTo(bestDiscount) > 0) {
                    bestDiscount = discount;
                    bestVoucher = voucher;
                }
            }
        }
        return bestVoucher;
    }

    private boolean isVoucherApplicable(Voucher voucher, BigDecimal totalAmount,
                                        Customer customer) {
        if (voucher.getStatus() != 1) {
            return false; // Voucher is not active
        }

        if (voucher.getStartDate().after(new Date()) || voucher.getEndDate().before(new Date())) {
            return false; // Voucher is not within the valid date range
        }

        if (totalAmount.compareTo(BigDecimal.valueOf(voucher.getMinimumTotalAmount())) < 0) {
            return false; // Bill amount is less than the minimum amount required
        }

        if (voucher.getApplyfor() == 1 && customer != null) {
            if (customer.getCustomerType() == null ||
                    !customerTypeVoucherRepository.findAllByVoucherId(voucher.getId()).stream()
                            .anyMatch(ctv -> ctv.getCustomerType()
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

            if (voucher.getMaximumReductionValue() != null || voucher.getMaximumReductionValue() != 0) {
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
