package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.nguyen.NCustomerTypeVoucherRepository;
import com.example.demo.repository.nguyen.NVoucherRepository;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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

    @Autowired
    NVoucherRepository voucherRepository;

    @Autowired
    NCustomerTypeVoucherRepository customerTypeVoucherRepository;


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
    @Transactional
    public Bill updateStatusAndBillStatus(Bill bill, Long id, BillHistory billHistory) {
        Optional<Bill> optionalBill = billRepository.findById(id);
        if (!optionalBill.isPresent()) {
            throw new EntityNotFoundException("Bill not found with id " + id);
        }

        Bill existingBill = optionalBill.get();
        existingBill.setReason(bill.getReason());
        existingBill.setStatus(bill.getStatus());
//        existingBill.setCustomer(null);

        addBillHistoryStatus(existingBill.getId(), billHistory.getStatus(),
                billHistory.getDescription(), 1, billHistory.getReason(),
                billHistory.getCreatedBy());

        updateQuantityProductDetail(existingBill.getId(), existingBill.getStatus());

        Bill returnBill = billRepository.save(existingBill);
        return returnBill;
    }

    @Override
    @Transactional
    public Bill updateShipmentDetail(Bill bill, Long id, String fullName) {
        Optional<Bill> optionalBill = billRepository.findById(id);
        if (!optionalBill.isPresent()) {
            throw new EntityNotFoundException("Bill not found with id " + id);
        }

        Bill existingBill = optionalBill.get();
        existingBill.setReciverName(bill.getReciverName());
        existingBill.setPhoneNumber(bill.getPhoneNumber());
        existingBill.setAddress(bill.getAddress());
        existingBill.setAddressId(bill.getAddressId());

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


    @Override
    public Bill setVoucherToBill(Long id, Voucher voucherRequest) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found"));
        Voucher voucher = voucherRepository.findById(voucherRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));

        if (bill.getVoucher() != null && bill.getVoucher().getId().equals(voucher.getId())) {
            voucher = null;
            bill.setTotalAmountAfterDiscount(bill.getTotalAmount());
        } else {
            BigDecimal discountValue = calculateDiscountVoucher(voucher, bill.getTotalAmount());
            bill.setTotalAmountAfterDiscount(bill.getTotalAmount().subtract(discountValue));
        }

        bill.setVoucher(voucher);

        return billRepository.save(bill);
    }


    private BigDecimal calculateDiscountVoucher(Voucher voucher, BigDecimal totalAmount) {
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
                return discountValue.min(BigDecimal.valueOf(voucher.getMaximumReductionValue()));
            }
            return discountValue;
        } else if (voucher.getDiscountType() == 2) { // Fixed amount discount
            discountValue = BigDecimal.valueOf(voucher.getValue());
            return discountValue.min(totalAmount); // Ensure discount doesn't exceed total amount
        }

        return BigDecimal.ZERO;
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

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found"));

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


//        bill.setStatus(newStatus);
//        billRepository.save(bill);
    }

    @Override
    @Transactional
    public Integer isQuantityExceedsProductDetail(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Bill not found with id " + billId));

        List<BillDetail> billDetails = bill.getBillDetail();
        for (BillDetail billDetail : billDetails) {
            ProductDetail productDetail = billDetail.getProductDetail();
            if (billDetail.getQuantity() > productDetail.getQuantity()) {
                return 1;
            }
        }
        return 0;
    }


    @Transactional
    //Hoàn lại số lượng trong productDetail khi hoàn trả
    public void refundProductDetailsQuantities(List<ReturnOrder> returnOrders) {
        for (ReturnOrder returnOrder : returnOrders) {
            BillDetail billDetail = returnOrder.getBillDetail();
            ProductDetail productDetail = billDetail.getProductDetail();
            productDetail.setQuantity(productDetail.getQuantity() + returnOrder.getQuantity());
            productDetail.setUpdatedAt(new Date());
            productDetailRepository.save(productDetail);
        }
    }

    @Transactional
    public void autoSetVoucher(Long billId) {
        Bill bill = billRepository.findById(billId).get();
        if (bill != null) return;
//        billRepository.findAll().forEach(bill -> {
        BigDecimal totalAmount = calculateTotalAmount(bill.getId());
        BigDecimal totalAmountSale = calculateTotalAmountSale(bill.getId());

        BigDecimal discount = BigDecimal.ZERO;
        bill.setTotalAmount(totalAmountSale);

        //lấy voucher tốt nhất
        Voucher bestVoucher = findBestVoucher(bill.getId());

        //tính giá trị có thế giảm từ voucher tốt nhất
        BigDecimal discountValue = calculateDiscount(bestVoucher, bill.getTotalAmount());

        Voucher oldVoucher = bill.getVoucher();
        BigDecimal oldDiscountValue = calculateDiscount(oldVoucher, bill.getTotalAmount());


        if (oldDiscountValue.compareTo(discountValue) == 0) {
            return;
        }
        if (discountValue.compareTo(oldDiscountValue) < 0) {
            return;
        }

//            System.out.println("-----------------------");
//            System.out.println(
//                    "Bill id: " + bill.getId() + " - tA: " + bill.getTotalAmount() + " - tAD: " +
//                            bill.getTotalAmountAfterDiscount());
//            System.out.println("best dis:" + discountValue);
//            System.out.println(
//                    "best vid: " + bestVoucher.getId() + ",value: " + bestVoucher.getValue() +
//                            " - " + bestVoucher.getDiscountType() + ",min : " +
//                            bestVoucher.getMinimumTotalAmount() + ", max: " +
//                            bestVoucher.getMaximumReductionValue());
//            System.out.println("old dis:" + oldDiscountValue);
//            System.out
//                    .println("old vid: " + oldVoucher.getId() + ",value: " + oldVoucher.getValue() +
//                            " - " + oldVoucher.getDiscountType() + ",min : " +
//                            oldVoucher.getMinimumTotalAmount() + ", max: " +
//                            oldVoucher.getMaximumReductionValue());

        bill.setTotalAmountAfterDiscount(totalAmountSale.subtract(discount));
        bill.setVoucher(bestVoucher);
        Bill saveBill = billRepository.save(bill);

        Voucher newVoucher = saveBill.getVoucher();

        if (!Objects.equals(newVoucher, oldVoucher)) {
            // Cập nhật chỉ khi voucher thực sự thay đổi
            bill.setVoucher(newVoucher);
            billRepository.save(bill);

            // Cập nhật số lượng voucher nếu oldVoucher không null
            if (oldVoucher != null) {
                oldVoucher.setQuantity(oldVoucher.getQuantity() + 1);
                voucherRepository.save(oldVoucher);
            }

            // Cập nhật số lượng voucher nếu newVoucher không null
            if (newVoucher != null) {
                newVoucher.setQuantity(newVoucher.getQuantity() - 1);
                voucherRepository.save(newVoucher);
            }
        }
//        });
    }

    public BigDecimal calculateTotalAmount(Long billId) {
        List<BillDetail> billDetails = billDetailRepository.findByBillId(billId);
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (BillDetail billDetail : billDetails) {
            BigDecimal detailTotal = billDetail.getPrice()
                    .multiply(BigDecimal.valueOf(billDetail.getQuantity()));
            totalAmount = totalAmount.add(detailTotal);
        }
        return totalAmount;
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
        if (totalAmount.compareTo(BigDecimal.valueOf(voucher.getMinimumTotalAmount())) < 0) {
            return BigDecimal.ZERO;
        }
        if (voucher.getDiscountType() == 1) {
            BigDecimal discountValue = totalAmount
                    .multiply(BigDecimal.valueOf(voucher.getValue() / 100.0));
            return discountValue.min(BigDecimal.valueOf(voucher.getMaximumReductionValue()));
        } else if (voucher.getDiscountType() == 2) {
            return BigDecimal.valueOf(voucher.getValue());
        }
        return BigDecimal.ZERO;
    }
//
//    @Transactional
//    public void recalculateTotalAmount(Long billId) {
//        Bill bill = billRepository.findById(billId)
//                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billId));
//
//        BigDecimal newTotalAmount = BigDecimal.ZERO;
//        BigDecimal newTotalAmountDiscount = BigDecimal.ZERO;
//
//        for (BillDetail detail : billDetailRepository.findAllByBillIdOrderByIdDesc(bill.getId())) {
//            BigDecimal priceToUse = detail.getPromotionalPrice().compareTo(BigDecimal.ZERO) > 0
//                    ? detail.getPromotionalPrice()
//                    : detail.getPrice();
//
//            BigDecimal detailTotal = priceToUse.multiply(BigDecimal.valueOf(detail.getQuantity()));
//            newTotalAmount = newTotalAmount.add(detailTotal);
//        }
//
//        newTotalAmountDiscount = calculateMaxDiscount(bill, bill.getCustomer(), newTotalAmount,
//                new Date());
//
//        bill.setTotalAmount(newTotalAmount);
//        bill.setTotalAmountAfterDiscount(newTotalAmount.subtract(newTotalAmountDiscount));
//        billRepository.save(bill);
//    }
//
//
//    @Transactional(readOnly = true)
//    public BigDecimal calculateMaxDiscount(Bill bill, Customer customer, BigDecimal totalAmount,
//                                           Date currentDate) {
//        List<Voucher> validVouchers = voucherRepository.findValidVouchers(currentDate, totalAmount);
//        Voucher maxDiscountVoucher = null;
//        BigDecimal maxDiscount = BigDecimal.ZERO;
//
//        for (Voucher voucher : validVouchers) {
//            if (isVoucherValid(voucher, customer, totalAmount, currentDate)) {
//                BigDecimal discount = calculateDiscount(voucher, totalAmount);
//                if (discount.compareTo(maxDiscount) > 0) {
//                    maxDiscount = discount;
//                    maxDiscountVoucher = voucher;
//                }
//            }
//        }
//
//        if (maxDiscountVoucher != null) {
//            bill.setVoucher(maxDiscountVoucher);
//            billRepository.save(bill);
//        }
//
//        return maxDiscount;
//    }
//
//    private boolean isVoucherValid(Voucher voucher, Customer customer, BigDecimal totalAmount,
//                                   Date currentDate) {
//        if (voucher.getStatus() != 1 ||
//                currentDate.before(voucher.getStartDate()) ||
//                currentDate.after(voucher.getEndDate()) ||
//                totalAmount.compareTo(BigDecimal.valueOf(voucher.getMinimumTotalAmount())) < 0) {
//            return false;
//        }
//
//        if (customer == null) {
//            return voucher.getApplyfor() == 0;
//        }
//
//        int usageCount = getVoucherUsageCount(voucher.getId(), customer.getId());
//        if (usageCount >= voucher.getNumberOfUses()) {
//            return false;
//        }
//
//        if (voucher.getApplyfor() == 0) {
//            return true;
//        } else if (voucher.getApplyfor() == 1) {
//            return isCustomerTypeEligible(voucher.getId(), customer.getCustomerType().getId());
//        }
//
//        return false;
//    }
//
//    @Transactional(readOnly = true)
//    public boolean isCustomerTypeEligible(Long voucherId, Long customerTypeId) {
//        return customerTypeVoucherRepository
//                .existsByVoucherIdAndCustomerTypeId(voucherId, customerTypeId);
//    }
//
//    @Transactional(readOnly = true)
//    public int getVoucherUsageCount(Long voucherId, Long customerId) {
//        return billRepository
//                .countByVoucherIdAndCustomerIdAndStatusNotIn(voucherId, customerId, Arrays
//                        .asList(5, 6));
//    }
//
//    private BigDecimal calculateDiscount(Voucher voucher, BigDecimal totalAmount) {
//        if (voucher.getDiscountType() == 1) {
//            // Percentage discount
//            BigDecimal discount = totalAmount.multiply(BigDecimal.valueOf(voucher.getValue()))
//                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
//
//            if (BigDecimal.valueOf(voucher.getMaximumReductionValue()).compareTo(BigDecimal.ZERO) >
//                    0) {
//                return discount.min(BigDecimal.valueOf(voucher.getMaximumReductionValue()));
//            }
//
//            return discount;
//        } else if (voucher.getDiscountType() == 2) {
//            // Fixed amount discount
//            return BigDecimal.valueOf(voucher.getValue()).min(totalAmount);
//        }
//        return BigDecimal.ZERO;
//    }

}
