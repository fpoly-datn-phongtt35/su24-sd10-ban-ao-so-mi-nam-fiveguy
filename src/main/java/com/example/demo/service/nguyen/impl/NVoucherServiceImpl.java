package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.*;
import com.example.demo.model.response.nguyen.CustomerVoucherStatsDTO;
import com.example.demo.model.response.nguyen.VoucherStatistics;
import com.example.demo.repository.nguyen.*;
import com.example.demo.repository.nguyen.bill.NBillDetailRepository;
import com.example.demo.service.nguyen.EmailService;
import com.example.demo.service.nguyen.NVoucherService;
//import org.hibernate.query.Page;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NVoucherServiceImpl implements NVoucherService {

    private static final String PREFIX = "PGG";
    private static final String DISCOUNT_TYPE_K = "K";
    private static final String APPLY_FOR_ALL = "_ALL";
    private static final String APPLY_FOR_LKH = "_LKH";
    private static final String APPLY_FOR_CN = "CN";

    @Autowired
    NVoucherRepository voucherRepository;

    @Autowired
    NCustomerTypeVoucherRepository customerTypeVoucherRepository;

    @Autowired
    NCustomerRepository customerRepository;

    @Autowired
    NBillDetailRepository billDetailRepository;

    @Autowired
    private com.example.demo.repository.nguyen.bill.NBillRepository billRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public List<Voucher> getAllVoucher() {
//        updateStatus();

        return voucherRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Voucher getVoucherById(Long id) {
//        updateStatus();

        return voucherRepository.findById(id).get();
    }

    @Override
    public Voucher createVoucher(Voucher voucher) {
        voucher.setCreatedAt(new Date());
        voucher.setCreatedBy("Admin add");
        voucher.setUpdatedAt(new Date());
        voucher.setUpdatedBy("Admin add");
        voucher.setStatus(checkDate(voucher.getStartDate(), voucher.getEndDate()));

        updateStatus();

        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher updateVoucher(Voucher voucher, Long id) {
        Optional<Voucher> voucherOptional = voucherRepository.findById(id);
        if (voucherOptional.isPresent()) {
            Voucher v = voucherOptional.get();
            v.setValue(voucher.getValue());
            v.setDiscountType(voucher.getDiscountType());
            v.setMaximumReductionValue(voucher.getMaximumReductionValue());
            v.setMinimumTotalAmount(voucher.getMinimumTotalAmount());
            v.setQuantity(voucher.getQuantity());
            v.setNumberOfUses(voucher.getNumberOfUses());
            v.setDescribe(voucher.getDescribe());
            v.setStartDate(voucher.getStartDate());
            v.setEndDate(voucher.getEndDate());
            v.setCreatedAt(voucher.getCreatedAt());
            v.setCreatedBy(voucher.getCreatedBy());
            v.setUpdatedAt(voucher.getUpdatedAt());
            v.setUpdatedBy(voucher.getUpdatedBy());
            v.setStatus(voucher.getStatus());

            updateStatus();

            return voucherRepository.save(v);
        }
        return null;
    }

    private Integer checkDate(Date startDate, Date endDate) {
        Date currentDate = new Date();
        if (startDate == null || endDate == null) return null;
        if (currentDate.after(startDate) && currentDate.before(endDate)) return 1;
        if (currentDate.after(endDate)) return 2;
        if (currentDate.before(startDate)) return 3;
        return null;
    }

    public void updateStatus() {
        for (Voucher v : voucherRepository.findAll()) {
            if (v.getStatus() != 4) {
                if (v.getStartDate() != null && v.getEndDate() != null) {
                    if (v.getStatus() != checkDate(v.getStartDate(), v.getEndDate())) {
                        v.setStatus(checkDate(v.getStartDate(), v.getEndDate()));
                        voucherRepository.save(v);
                    }
                }
            }
            Date currentDate = new Date();
            if (v.getStatus() == 4 && currentDate.after(v.getEndDate())) {
                v.setStatus(checkDate(v.getStartDate(), v.getEndDate()));
                voucherRepository.save(v);
            }
        }

//            Voucher voucher = voucherRepository.findById(voucherRequest.getId())
//                    .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));
//
//            if (bill.getVoucher() != null && bill.getVoucher().getId().equals(voucher.getId())) {
//                voucher = null;
//                bill.setTotalAmountAfterDiscount(bill.getTotalAmount());
//
//
//            bill.setVoucher(voucher);
//
//            billRepository.save(bill);
    }

    @Scheduled(fixedRate = 60000)
    public void autoUpdateStatus() {
        updateStatus();
        updateBillsWithNewVouchers();
    }

    @Transactional
    public void updateBillsWithNewVouchers() {
        List<Bill> billsToUpdate = findBillsWithInvalidVouchers();

        for (Bill bill : billsToUpdate) {
            Voucher bestVoucher = findBestVoucher(bill);
            updateBillWithNewVoucher(bill, bestVoucher);
        }
    }

    List<Bill> findBillsWithInvalidVouchers() {
        Date currentDate = new Date();
        return billRepository.findAll().stream()
                .filter(bill -> bill.getVoucher() != null &&
                        (bill.getVoucher().getStatus() == 2 ||
                                bill.getVoucher().getEndDate().before(currentDate)))
                .collect(Collectors.toList());
    }

    private void updateBillWithNewVoucher(Bill bill, Voucher newVoucher) {
        bill.setVoucher(newVoucher);
        if (newVoucher != null) {
            BigDecimal newTotalAmountAfterDiscount = calculateNewTotalAmountAfterDiscount(bill,
                    newVoucher);
            bill.setTotalAmountAfterDiscount(newTotalAmountAfterDiscount);
        } else {
            bill.setTotalAmountAfterDiscount(bill.getTotalAmount());
        }
        billRepository.save(bill);
    }

    private BigDecimal calculateNewTotalAmountAfterDiscount(Bill bill, Voucher voucher) {
        BigDecimal totalAmount = bill.getTotalAmount();
        BigDecimal discount = calculateDiscount(voucher, totalAmount);
        return totalAmount.subtract(discount);
    }

    @Transactional(readOnly = true)
    public Voucher findBestVoucher(Bill bill) {
        BigDecimal totalAmount = bill.getTotalAmount();
        List<Voucher> vouchers = voucherRepository.findAll();

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

    @Override
    public Page<Voucher> findVouchers(String code, String name, Integer applyfor,
                                      Integer discountType, Date startDate, Date endDate,
                                      Integer status, Pageable pageable) {
        Specification<Voucher> spec = Specification
                .where(VoucherSpecification.hasCodeOrName(code))
//        Specification<Voucher> spec = Specification.where(VoucherSpecification.hasCode(code == null ? code : code.toUpperCase()))
//                .and(VoucherSpecification.hasName(name))
                .and(VoucherSpecification.hasApplyfor(applyfor))
                .and(VoucherSpecification.hasDiscountType(discountType))
                .and(VoucherSpecification.hasStartDate(startDate))
                .and(VoucherSpecification.hasEndDate(endDate))
                .and(VoucherSpecification.hasStatus(status));

        Pageable sortedPageable = PageRequest
                .of(pageable.getPageNumber(), pageable.getPageSize(),
                        Sort.by(Sort.Order.desc("CreatedAt")));
        return voucherRepository.findAll(spec, sortedPageable);
    }

    @Override
    public Voucher createVoucher(Voucher voucher, List<CustomerType> customerTypeList) {
        if (voucher.getCode() == null || voucher.getCode() == "" ||
                voucher.getCode().trim().isBlank() || voucher.getCode().trim().isEmpty()) {
            String uniqueCode = generateVoucherCode(voucher.getValue(), voucher.getDiscountType(),
                    voucher.getApplyfor());
            voucher.setCode(uniqueCode);
        } else {
            voucher.setCode(voucher.getCode().trim().toUpperCase());
        }
        voucher.setName(voucher.getName().trim());
        voucher.setCreatedAt(new Date());
        voucher.setCreatedBy(voucher.getCreatedBy());
        voucher.setUpdatedAt(new Date());
        voucher.setUpdatedBy(voucher.getCreatedBy());
        voucher.setStatus(checkDate(voucher.getStartDate(), voucher.getEndDate()));

        updateStatus();

        Voucher returnVoucher = voucherRepository.save(voucher);

        for (CustomerType customerType :
                customerTypeList) {
            CustomerTypeVoucher customerTypeVoucher = new CustomerTypeVoucher();
            customerTypeVoucher.setVoucher(returnVoucher);
            customerTypeVoucher.setCustomerType(customerType);
            customerTypeVoucher.setCreatedAt(new Date());
            customerTypeVoucher.setUpdatedAt(new Date());
            customerTypeVoucher.setStatus(1);

            customerTypeVoucherRepository.save(customerTypeVoucher);

            if (voucher.getApplyfor() == 1) {
                List<Customer> customers = customerRepository
                        .findByCustomerTypeId(customerType.getId());
                for (Customer customer : customers) {
                    if (customer.getAccount() != null && customer.getAccount().getEmail() != null) {
                        String email = customer.getAccount().getEmail();
                        String subject = "Phiếu Giảm Giá Mới: " + returnVoucher.getName();
                        String htmlContent = buildVoucherEmailContent(customer, returnVoucher);
                        emailService.sendHtmlMessage(email, subject, htmlContent);
                    }
                }
            }
        }

        return returnVoucher;
    }

    @Override
    public Voucher updateVoucher(Long voucherId, Voucher updatedVoucher,
                                 List<CustomerType> updatedCustomerTypeList) {
        // Retrieve the existing voucher by ID
        Optional<Voucher> optionalVoucher = voucherRepository.findById(voucherId);
        if (!optionalVoucher.isPresent()) {
            throw new EntityNotFoundException("Voucher not found with id " + voucherId);
        }

        Voucher existingVoucher = optionalVoucher.get();

        // Update voucher details

        existingVoucher.setCode(updatedVoucher.getCode().trim().toUpperCase());
        existingVoucher.setName(updatedVoucher.getName().trim());
        existingVoucher.setValue(updatedVoucher.getValue());
        existingVoucher.setApplyfor(updatedVoucher.getApplyfor());
        existingVoucher.setDiscountType(updatedVoucher.getDiscountType());
        existingVoucher
                .setMaximumReductionValue(updatedVoucher.getMaximumReductionValue());
        existingVoucher.setMinimumTotalAmount(updatedVoucher.getMinimumTotalAmount());
        existingVoucher.setQuantity(updatedVoucher.getQuantity());
        existingVoucher.setNumberOfUses(updatedVoucher.getNumberOfUses());
        existingVoucher.setDescribe(updatedVoucher.getDescribe());
//        existingVoucher.setStartDate(updatedVoucher.getStartDate());
        existingVoucher.setEndDate(updatedVoucher.getEndDate());
        existingVoucher.setCreatedAt(updatedVoucher.getCreatedAt());
        existingVoucher.setCreatedBy(updatedVoucher.getCreatedBy());
        existingVoucher.setUpdatedAt(new Date());
        existingVoucher.setUpdatedBy(updatedVoucher.getUpdatedBy());
        existingVoucher.setStatus(updatedVoucher.getStatus() == 4 ? 4 : checkDate(
                updatedVoucher.getStartDate(), updatedVoucher.getEndDate()));

        updateStatus();

        Voucher returnVoucher = voucherRepository.save(existingVoucher);

        // Remove existing customer type vouchers associated with the voucher
        List<CustomerTypeVoucher> existingCustomerTypeVouchers = customerTypeVoucherRepository
                .findAllByVoucherId(voucherId);
        for (CustomerTypeVoucher existingCustomerTypeVoucher : existingCustomerTypeVouchers) {
            customerTypeVoucherRepository.delete(existingCustomerTypeVoucher);
        }

        // Add updated customer type vouchers
        for (CustomerType updatedCustomerType : updatedCustomerTypeList) {
            CustomerTypeVoucher customerTypeVoucher = new CustomerTypeVoucher();
            customerTypeVoucher.setVoucher(returnVoucher);
            customerTypeVoucher.setCustomerType(updatedCustomerType);
            customerTypeVoucher.setCreatedAt(new Date());
            customerTypeVoucher.setUpdatedAt(new Date());
            customerTypeVoucher.setStatus(1);

            customerTypeVoucherRepository.save(customerTypeVoucher);
        }

        return returnVoucher;
    }

    @Override
    public VoucherStatistics calculateVoucherStatistics(Long voucherId) {
        Voucher voucher = voucherRepository.findByIdN(voucherId);

        if (voucher == null) {
            throw new IllegalArgumentException("Voucher not found");
        }

        List<Bill> bills = billRepository.findByVoucherId(voucherId);
        int usageCount = bills.size();

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalRevenueAfterDiscount = BigDecimal.ZERO;

        for (Bill bill : bills) {
            totalRevenue = totalRevenue.add(bill.getTotalAmount());
            totalRevenueAfterDiscount = totalRevenueAfterDiscount
                    .add(bill.getTotalAmountAfterDiscount());
        }

        BigDecimal profit = totalRevenueAfterDiscount.subtract(totalRevenue);
        BigDecimal profitMargin = (totalRevenue.compareTo(BigDecimal.ZERO) > 0) ? profit
                .divide(totalRevenue, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;

        return new VoucherStatistics(usageCount, totalRevenue, totalRevenueAfterDiscount,
                profit, profitMargin);
    }

    @Override
    public Page<CustomerVoucherStatsDTO> getCustomerVoucherStats(Long voucherId,
                                                                 Pageable pageable) {
        return billRepository.findCustomerVoucherStatsByVoucherId(voucherId, pageable);
    }

    private String generateVoucherCode(Double value, Integer discountType, Integer applyfor) {
        StringBuilder codeBuilder = new StringBuilder(PREFIX);

        // Convert the value to an integer string
        String valueStr = Integer.toString(value.intValue());
        codeBuilder.append(valueStr);

        if (discountType != null && discountType == 2) {
            // Remove last 3 digits and append "K"
            if (valueStr.length() > 3) {
                codeBuilder.setLength(codeBuilder.length() - 3);
            }
            codeBuilder.append(DISCOUNT_TYPE_K);
        }

        switch (applyfor) {
            case 0:
                codeBuilder.append(APPLY_FOR_ALL);
                break;
            case 1:
                codeBuilder.append(APPLY_FOR_LKH);
                break;
        }

        String baseCode = codeBuilder.toString();
        String finalCode = baseCode + "001";

        int counter = 1;
        while (voucherRepository.existsByCode(finalCode)) {
            counter++;
            finalCode = baseCode + String.format("%03d", counter);
        }

        return finalCode;
    }

    private String buildVoucherEmailContent(Customer customer, Voucher voucher) {
        String discountType = voucher.getDiscountType() == 1 ? "%" : "đồng";
        String valueWithType =
                voucher.getDiscountType() == 1 ? (voucher.getValue() + " " +
                        discountType) : formatCurrency(voucher.getValue());
        String maxReductionValue = formatCurrency(voucher.getMaximumReductionValue());
        String minTotalAmount = formatCurrency(voucher.getMinimumTotalAmount());
        String startDate = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy")
                .format(voucher.getStartDate());
        String endDate = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(voucher.getEndDate());

        String htmlContent = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1);'>" +
                "<h2 style='color: #4CAF50; text-align: center; font-size: 24px;'>Kính chào " +
                customer.getFullName() + ",</h2>" +
                "<p style='text-align: center; font-size: 18px;'>Chúng tôi rất vui mừng được gửi đến bạn một phiếu giảm giá mới!</p>" +
                "<div style='border: 2px dashed #4CAF50; padding: 20px; margin: 20px 0; text-align: center;'>" +
                "<h1 style='color: #4CAF50; margin: 0; font-size: 36px;'>" + voucher.getCode() +
                "</h1>" +
                "<h2 style='color: #000; margin: 10px 0; font-size: 30px;'> Giảm " + valueWithType +
                "</h2>" +
                "</div>" +
                "<div style='text-align: center;'>" +
                (voucher.getDiscountType() == 1 ?
                        "<p style='color: #555; font-size: 20px; margin: 10px 0;'>Giảm tối đa: " +
                                maxReductionValue + "</p>" : "") +
                "<p style='color: #555; font-size: 20px; margin: 10px 0;'>Cho đơn hàng từ: " +
                minTotalAmount + "</p>" +
                "<p style='color: #555; font-size: 20px; margin: 10px 0;'>Hiệu lực từ: " +
                startDate + " đến " + endDate + "</p>" +
                "</div>" +
                "<p style='text-align: center; font-size: 18px;'>Đừng bỏ lỡ cơ hội tiết kiệm này!</p>" +
                "<p style='text-align: center; font-size: 18px;'>Trân trọng,</p>" +
                "<p style='text-align: center; color: #4CAF50; font-size: 18px;'>Công ty của bạn</p>" +
                "</div>" +
                "</body>" +
                "</html>";
        return htmlContent;
    }

    private String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount) + " đồng";
    }

    @Override
    @Transactional(readOnly = true)
    public List<Voucher> findAllVoucherCanUse(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() ->  new RuntimeException("Bill not found"));

        BigDecimal totalAmount = calculateTotalAmountSale(billId);
        List<Voucher> vouchers = voucherRepository
                .findAll(); // Assuming you have a method to fetch all vouchers

        List<Voucher> listVoucher = new ArrayList<>();

        for (Voucher voucher : vouchers) {
            if (isVoucherApplicable(voucher, totalAmount, bill.getCustomer(), bill)) {
                listVoucher.add(voucher);
            }
        }
//
//        int count = 0;
//        for (Voucher voucher: listVoucher){
//            if(voucher == bill.getVoucher()){
//                count++;
//            }
//        }
//        if(count == 0){
//            listVoucher.add(bill.getVoucher());
//        }

        listVoucher.sort((v1, v2) -> calculateDiscount(v2, totalAmount)
                .compareTo(calculateDiscount(v1, totalAmount)));

        return listVoucher;
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
        // Check if the voucher is already used in the current bill
        boolean isCurrentBillUsingVoucher =
                bill.getVoucher() != null && bill.getVoucher().getId().equals(voucher.getId());

        if (voucher.getQuantity() <= 0 && !isCurrentBillUsingVoucher) {
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

            if (usedCount >= voucher.getNumberOfUses() && !isCurrentBillUsingVoucher) {
                return false; // Voucher usage limit reached
            }
        }

//        // Check the number of uses limit using repository
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

            if (voucher.getMaximumReductionValue() != null &&
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
}
