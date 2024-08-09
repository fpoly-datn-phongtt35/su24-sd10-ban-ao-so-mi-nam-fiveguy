package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.*;
import com.example.demo.model.response.nguyen.BillResponse;
import com.example.demo.repository.nguyen.NCustomerTypeVoucherRepository;
import com.example.demo.repository.nguyen.NVoucherRepository;
import com.example.demo.repository.nguyen.bill.*;
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

    @Autowired
    NPaymentStatusRepository paymentStatusRepository;

    @Autowired
    NReturnOrderRepository returnOrderRepository;

    @Override
    public List<Bill> getAll() {
        return billRepository.findAll();
    }

    @Override
    public Bill getById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() ->  new RuntimeException("Bill not found"));
        return bill;
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

        if (bill.getStatus() == 2) {
            existingBill.setShippingFee(bill.getShippingFee());
        }
//        existingBill.setCustomer(null);

        addBillHistoryStatus(existingBill.getId(), billHistory.getStatus(),
                billHistory.getDescription(), 1, billHistory.getReason(),
                billHistory.getCreatedBy());

        updateQuantityProductDetail(existingBill.getId(), existingBill.getStatus());

        Bill returnBill = billRepository.save(existingBill);


        if (returnBill.getStatus() == 2) {
//            changePricePayment(returnBill, returnBill.getTotalAmountAfterDiscount());
//            processAndSavePaymentStatuses(returnBill);
            confirmBillAndUpdateVoucher(returnBill.getId());
        }
        if (returnBill.getStatus() == 5 || returnBill.getStatus() == 6) {
            updateVoucherOnBillCancellation(returnBill.getId());
        }
        if (returnBill.getStatus() == 32 || returnBill.getStatus() == 12) {
            refundProductDetailsQuantities(returnBill);
        }


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


    @Override
    public Bill updateShippingFee(Long id, BigDecimal shippingFee) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bill ID"));
        bill.setShippingFee(shippingFee);

        return billRepository.save(bill);
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


    //region Chọn voucher từ trong list Voucher có thể dùng
    @Override
    @Transactional
    public Bill setVoucherToBill(Long id, Voucher voucherRequest) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found"));
        Voucher voucher = voucherRepository.findById(voucherRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));

        if (bill.getVoucher() != null && bill.getVoucher().getId().equals(voucher.getId())) {
            voucher = null;
            bill.setTotalAmountAfterDiscount(bill.getTotalAmount());

//            changePricePayment(bill, bill.getTotalAmount());
        } else {
            BigDecimal discountValue = calculateDiscountVoucher(voucher, bill.getTotalAmount());
            bill.setTotalAmountAfterDiscount(bill.getTotalAmount().subtract(discountValue));

//            changePricePayment(bill, bill.getTotalAmount().subtract(discountValue));
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
    //endregion

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

//        if(bill.getStatus() != 1 || bill.getStatus() != 2) return 0;

        List<BillDetail> billDetails = bill.getBillDetail();
        for (BillDetail billDetail : billDetails) {
            ProductDetail productDetail = billDetail.getProductDetail();
            if ((billDetail.getQuantity() > productDetail.getQuantity() ||
                    billDetail.getQuantity() < 0) && productDetail.getQuantity() > 0) {
                return 1;
            } else if (productDetail.getQuantity() <= 0) {
                return 2;
            }
        }
        return 0;
    }


    //Hoàn lại số lượng trong productDetail khi hoàn trả
    public void refundProductDetailsQuantities(Bill bill) {
        List<ReturnOrder> returnOrders = returnOrderRepository
                .findAllReturnOrdersByBillIdOrderByCreatedAtDesc(bill.getId());

        for (ReturnOrder returnOrder : returnOrders) {
            BillDetail billDetail = returnOrder.getBillDetail();
            ProductDetail productDetail = billDetail.getProductDetail();
            productDetail.setQuantity(productDetail.getQuantity() + returnOrder.getQuantity());
            productDetail.setUpdatedAt(new Date());
            productDetailRepository.save(productDetail);
        }
    }

    //Hoàn lại số lượng trong productDetail khi hoàn trả
    public void refundProductDetailsQuantities______new(Bill bill) {
        List<ReturnOrder> returnOrders = returnOrderRepository
                .findAllReturnOrdersByBillIdOrderByCreatedAtDesc(bill.getId());

        for (ReturnOrder returnOrder : returnOrders) {
            BillDetail billDetail = returnOrder.getBillDetail();
            ProductDetail productDetail = billDetail.getProductDetail();

            // Hoàn lại số lượng trong ProductDetail
            productDetail.setQuantity(productDetail.getQuantity() + returnOrder.getQuantity());
            productDetail.setUpdatedAt(new Date());
            productDetailRepository.save(productDetail);

            // Giảm số lượng trong BillDetail
//            billDetail.setQuantity(billDetail.getQuantity() - returnOrder.getQuantity());

            // Kiểm tra nếu số lượng trong BillDetail bằng 0 thì xóa
            if (billDetail.getQuantity() == 0) {
                billDetailRepository.delete(billDetail);
            }
        }
    }


    //region Update voucher khi trạng thái sang 2 hoặc 5,6
    @Transactional
    public void confirmBillAndUpdateVoucher(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        // Chỉ xử lý khi trạng thái bill chuyển từ 1 sang 2
        if (bill.getStatus() != 2) {
            throw new RuntimeException("Bill status is not valid for confirmation");
        }

        Voucher usedVoucher = bill.getVoucher();
        if (usedVoucher != null) {
            // Giảm số lượng voucher còn lại
            usedVoucher.setQuantity(usedVoucher.getQuantity() - 1);
            voucherRepository.save(usedVoucher);

            // Nếu voucher này còn 0 cái sau khi xác nhận
            if (usedVoucher.getQuantity() == 0) {
                // Tìm các bill khác đang sử dụng voucher này và có trạng thái là 1
                List<Bill> affectedBills = billRepository
                        .findByVoucherIdAndStatus(usedVoucher.getId(), 1);

                for (Bill affectedBill : affectedBills) {
                    // Tìm voucher tốt nhất mới cho bill này
                    Voucher newBestVoucher = findBestVoucher(affectedBill.getId());

                    // Cập nhật voucher mới cho bill
                    affectedBill.setVoucher(newBestVoucher);
                    billRepository.save(affectedBill);
                }
            }
        }

        // Cập nhật trạng thái bill sang 2
//        bill.setStatus(2);
//        billRepository.save(bill);
    }

    @Transactional
    public void updateVoucherOnBillCancellation(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        // Chỉ xử lý khi trạng thái bill chuyển sang 5 hoặc 6
        if (bill.getStatus() != 5 && bill.getStatus() != 6) {
            throw new RuntimeException("Bill status is not valid for cancellation or return");
        }

        Voucher usedVoucher = bill.getVoucher();
        if (usedVoucher != null) {
            // Tăng số lượng voucher lên 1
            usedVoucher.setQuantity(usedVoucher.getQuantity() + 1);
            voucherRepository.save(usedVoucher);

            // Nếu voucher này vừa tăng từ 0 lên 1
//            if (usedVoucher.getQuantity() == 1) {
//                // Tìm các bill khác đang có trạng thái là 1 (chờ xác nhận)
//                List<Bill> pendingBills = billRepository.findByStatus(1);
//
//                for (Bill pendingBill : pendingBills) {
//                    // Tìm voucher tốt nhất mới cho bill này (có thể bao gồm voucher vừa được khôi phục)
//                    Voucher newBestVoucher = findBestVoucher(pendingBill.getId());
//
//                    // Nếu voucher tốt nhất mới khác với voucher hiện tại của bill
//                    if (!Objects.equals(pendingBill.getVoucher(), newBestVoucher)) {
//                        // Cập nhật voucher mới cho bill
//                        pendingBill.setVoucher(newBestVoucher);
//                        billRepository.save(pendingBill);
//                    }
//                }
//            }
        }

        // Cập nhật trạng thái bill (nếu cần)
        // bill.setStatus(5); hoặc bill.setStatus(6);
//        billRepository.save(bill);
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
            if (customer == null || customer.getCustomerType() == null ||
                    !customerTypeVoucherRepository.findAllByVoucherId(voucher.getId()).stream()
                            .anyMatch(ctv -> ctv.getCustomerType()
                                    .equals(customer.getCustomerType()))) {
                return false; // Customer type does not match for applyfor = 1
            }
        }

        // Check the number of uses limit using repository
        if (voucher.getNumberOfUses() != null && customer != null) {
            long usedCount = billRepository
                    .countByCustomerIdAndVoucherIdAndStatusNotIn(customer.getId(), voucher.getId(),
                            List.of(5, 6, 1));  //Bỏ 1 nếu muốn hiển thị khi voucher chưa xác nhận

            // Check if the voucher is already used in the current bill
            boolean isCurrentBillUsingVoucher =
                    bill.getVoucher() != null && bill.getVoucher().getId().equals(voucher.getId());

            if (usedCount >= voucher.getNumberOfUses() && !isCurrentBillUsingVoucher) {
                return false; // Voucher usage limit reached
            }
        }

        return true; // Voucher is applicable
    }

    private BigDecimal calculateDiscount(Voucher voucher, BigDecimal totalAmount) {
        if (voucher == null || totalAmount == null) {
            return BigDecimal.ZERO;
        }

        //Update phần này nếu trừ số lượng lúc tạo đơn ***
        if (voucher.getQuantity() <= 0) {
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
    //endregion


    private void changePricePayment(Bill bill, BigDecimal totalAmount) {

        if (bill == null) return;

        List<PaymentStatus> paymentStatuses = paymentStatusRepository
                .findAllByBillIdAndCustomerPaymentStatusOrderByIdAsc(bill.getId(), 1);

        if (paymentStatuses.isEmpty()) return;

        PaymentStatus paymentStatus = paymentStatuses.get(0);
//        for (PaymentStatus ps : paymentStatuses){
//            if(ps.getPaymentType() == 1){
//                paymentStatus = ps;
//            }
//        }

        if (paymentStatus != null) {
            paymentStatus.setPaymentAmount(totalAmount.add(bill.getShippingFee()));
            paymentStatusRepository.save(paymentStatus);
        }
    }

    @Transactional
    public void processAndSavePaymentStatuses(Bill bill) {
        if (bill == null) return;

        List<PaymentStatus> paymentStatuses = paymentStatusRepository
                .findAllByBillIdOrderByIdAsc(bill.getId());

        boolean hasCustomerPayment = false;
        for (PaymentStatus ps : paymentStatuses) {
            if (ps.getCustomerPaymentStatus() == 1 && ps.getPaymentType() == 1) {
                hasCustomerPayment = true;
                break;
            }
        }

        if (hasCustomerPayment) {
            for (PaymentStatus ps : paymentStatuses) {
                if (ps.getCustomerPaymentStatus() == 1 && ps.getPaymentType() == 1) {
                    ps.setPaymentAmount(
                            bill.getTotalAmountAfterDiscount().add(bill.getShippingFee()));
                    paymentStatusRepository.save(ps);
                }
            }
        } else {
            PaymentStatus customerPayment = null;
            for (PaymentStatus ps : paymentStatuses) {
                if (ps.getCustomerPaymentStatus() == 2 && ps.getPaymentType() == 1) {
                    customerPayment = ps;
                    break;
                }
            }

            if (customerPayment != null) {
                BigDecimal totalDue = bill.getTotalAmountAfterDiscount().add(bill.getShippingFee());
                int comparisonResult = customerPayment.getPaymentAmount().compareTo(totalDue);

                if (comparisonResult > 0) {
                    PaymentStatus refundStatus = new PaymentStatus();
                    refundStatus.setCode(generateCode(3));
                    refundStatus.setPaymentDate(new Date());
                    refundStatus.setPaymentType(3);
                    refundStatus.setCustomerPaymentStatus(4);
                    refundStatus.setPaymentAmount(
                            customerPayment.getPaymentAmount().subtract(totalDue));
                    refundStatus.setBill(bill);
                    paymentStatusRepository.save(refundStatus);
                } else if (comparisonResult < 0) {
                    PaymentStatus remainingStatus = new PaymentStatus();
                    remainingStatus.setCode(generateCode(1));
                    remainingStatus.setPaymentDate(new Date());
                    remainingStatus.setPaymentType(1);
                    remainingStatus.setCustomerPaymentStatus(1);
                    remainingStatus.setPaymentAmount(
                            totalDue.subtract(customerPayment.getPaymentAmount()));
                    remainingStatus.setBill(bill);
                    paymentStatusRepository.save(remainingStatus);
                }
                // Nếu comparisonResult == 0, không làm gì cả
            }
        }

        billRepository.save(bill);
    }


    private Set<String> generatedCodes = new HashSet<>();
    private Random random = new Random();

    public String generateCode(int input) {
        String prefix;
        if (input == 1) {
            prefix = "TT";
        } else if (input == 3) {
            prefix = "HT";
        } else {
            throw new IllegalArgumentException("Invalid input, must be 1 or 3");
        }

        String code;
        do {
            code = prefix + String.format("%04d", random.nextInt(10000));
        } while (generatedCodes.contains(code));

        generatedCodes.add(code);
        return code;
    }


    //    Hải code
    @Override
    public Page<BillResponse> getBillsByFilters(List<Integer> statuses, String searchTerm,
                                                Integer typeBill, Date fromDate, Date toDate,
                                                Pageable pageable) {
        Page<Bill> bills = billRepository
                .findBillsByFilters(statuses, searchTerm, typeBill, fromDate, toDate, pageable);
        return bills.map(this::toBillResponse);
    }

    private BillResponse toBillResponse(Bill bill) {
        BillResponse response = new BillResponse();
        response.setId(bill.getId());
        response.setCode(bill.getCode());
        response.setTotalAmount(bill.getTotalAmount());

        // Set other fields as needed
        response.setNameCustomer(
                bill.getCustomer() != null && bill.getCustomer().getFullName() != null
                        ? bill.getCustomer().getFullName()
                        : "Khách lẻ");
        response.setCreatedAt(bill.getCreatedAt()); // Convert Date to SQL Date
        response.setTypeBill(bill.getTypeBill());
        response.setStatus(bill.getStatus());
        response.setPaidAmount(bill.getPaidAmount());

        // Sum quantity from BillDetail
        Integer quantity = billDetailRepository.sumQuantityByBillId(bill.getId());
        response.setQuantity(quantity != null ? quantity : 0);

        return response;
    }

}
