package com.example.demo.service.common.impl;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Voucher;
import com.example.demo.model.response.common.VoucherDTO;
import com.example.demo.repository.common.CustomerCommonRepository;
import com.example.demo.repository.common.CustomerTypeVouchersCommonRepository;
import com.example.demo.repository.common.VoucherCommonRepository;
import com.example.demo.service.common.VoucherCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class VoucherCommonServiceImpl implements VoucherCommonService {

    @Autowired
    private VoucherCommonRepository voucherCommonRepository;

    @Autowired
    private CustomerTypeVouchersCommonRepository customerTypeVouchersRepository;

    @Autowired
    private CustomerCommonRepository customerCommonRepository;

    @Autowired
    private BillCommonServiceImpl billServiceCommonImpl;

    @Override
    public List<Voucher> getAllVouchersByStatusAndApplyFor() {
        return voucherCommonRepository.findAllByStatusAndApplyFor();
    }



    @Override
    public List<VoucherDTO> getVouchersForCustomer(Customer customer, String search) {
        // Criterion 1: get vouchers by CustomerTypeVouchers
        List<Long> voucherIdsFromCustomerType = customerTypeVouchersRepository.findVoucherIdsByCustomerTypeId(customer.getCustomerType().getId());
        List<Voucher> vouchersFromCustomerType = voucherCommonRepository.findAllByIdAndStatus(voucherIdsFromCustomerType);

        // Criterion 2: get vouchers by applyfor all
        List<Voucher> voucherStatus0 = voucherCommonRepository.findAllByStatus1AndApplyFor();

        // Combine both lists
        List<Voucher> combinedVouchers = new ArrayList<>();
        combinedVouchers.addAll(vouchersFromCustomerType);
        combinedVouchers.addAll(voucherStatus0);

        // Remove duplicates
        combinedVouchers = combinedVouchers.stream().distinct().collect(Collectors.toList());

        // Filter by voucher name or code
        if (search != null && !search.isEmpty()) {
            combinedVouchers = combinedVouchers.stream()
                    .filter(voucher -> voucher.getName().toLowerCase().contains(search.toLowerCase())
                            || voucher.getCode().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Sort by maximumReductionValue in descending order
        combinedVouchers.sort(Comparator.comparing(Voucher::getMaximumReductionValue).reversed());

        // Convert List<Voucher> to List<VoucherDTO>
        List<VoucherDTO> voucherDTOs = combinedVouchers.stream()
                .map(voucher -> new VoucherDTO(
                        voucher.getId(),
                        voucher.getCode(),
                        voucher.getName(),
                        voucher.getValue(),
                        voucher.getDiscountType(),
                        voucher.getMaximumReductionValue(),
                        voucher.getMinimumTotalAmount(),
                        voucher.getQuantity(),
                        voucher.getDescribe(),
                        voucher.getEndDate(),
                        checkNumberOfUser(customer,voucher)
                ))
                .collect(Collectors.toList());

        return voucherDTOs;
    }

    public  Integer checkNumberOfUser(Customer customer,Voucher voucher){
           Integer countUse = billServiceCommonImpl.countVoucherUsageByCustomer(customer.getId(),voucher.getId());
        System.out.println(countUse);
        System.out.println(voucher.getNumberOfUses());
           if (countUse > voucher.getNumberOfUses()){
               return 2;
           }
           return 1;
        }


//        Lấy list voucher cho bán tại quầy truyền tạo controller chọn khách hàng có id truyền vào
//    ----
//    ----
//    ----
    @Override
    public List<VoucherDTO> getVouchersForCustomer(Long id, String search) {
        // Fetch the customer
        Optional<Customer> customer = customerCommonRepository.findById(id);

        if (customer.isPresent()){
            // Criterion 1: get vouchers by CustomerTypeVouchers
            List<Long> voucherIdsFromCustomerType = customerTypeVouchersRepository.findVoucherIdsByCustomerTypeId(customer.get().getCustomerType().getId());
            List<Voucher> vouchersFromCustomerType = voucherCommonRepository.findAllByIdAndStatus(voucherIdsFromCustomerType);

            // Criterion 2: get vouchers by applyfor all
            List<Voucher> voucherStatus0 = voucherCommonRepository.findAllByStatus1AndApplyFor();

            // Combine both lists
            List<Voucher> combinedVouchers = new ArrayList<>();
            combinedVouchers.addAll(vouchersFromCustomerType);
            combinedVouchers.addAll(voucherStatus0);

            // Remove duplicates
            combinedVouchers = combinedVouchers.stream().distinct().collect(Collectors.toList());

            // Filter by voucher name or code
            if (search != null && !search.isEmpty()) {
                combinedVouchers = combinedVouchers.stream()
                        .filter(voucher -> voucher.getName().toLowerCase().contains(search.toLowerCase())
                                || voucher.getCode().toLowerCase().contains(search.toLowerCase()))
                        .collect(Collectors.toList());
            }

            // Sort by maximumReductionValue in descending order
            combinedVouchers.sort(Comparator.comparing(Voucher::getMaximumReductionValue).reversed());

            // Convert List<Voucher> to List<VoucherDTO>
            List<VoucherDTO> voucherDTOs = combinedVouchers.stream()
                    .map(voucher -> new VoucherDTO(
                            voucher.getId(),
                            voucher.getCode(),
                            voucher.getName(),
                            voucher.getValue(),
                            voucher.getDiscountType(),
                            voucher.getMaximumReductionValue(),
                            voucher.getMinimumTotalAmount(),
                            voucher.getQuantity(),
                            voucher.getDescribe(),
                            voucher.getEndDate(),
//                            1 hiển thị đc
//                            2 vượt quá số lượng giới hạn voucher
                            checkNumberOfUser(customer.get(),voucher)
                    ))
                    .collect(Collectors.toList());

            return voucherDTOs;
        }
        return null;

    }


}
