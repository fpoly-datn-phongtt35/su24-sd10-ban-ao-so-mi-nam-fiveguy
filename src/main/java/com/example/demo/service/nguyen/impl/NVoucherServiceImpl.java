package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.*;
import com.example.demo.model.response.nguyen.CustomerVoucherStatsDTO;
import com.example.demo.model.response.nguyen.VoucherStatistics;
import com.example.demo.repository.nguyen.*;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    private com.example.demo.repository.nguyen.bill.NBillRepository billRepository;

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
        if(startDate == null || endDate == null) return null;
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
    }

    @Scheduled(fixedRate = 60000)
    public void autoUpdateStatus() {
        updateStatus();
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
        if (voucher.getCode() == null) {
            String uniqueCode = generateVoucherCode(voucher.getValue(), voucher.getDiscountType(),
                    voucher.getApplyfor());
            voucher.setCode(uniqueCode);
        }else{
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
}
