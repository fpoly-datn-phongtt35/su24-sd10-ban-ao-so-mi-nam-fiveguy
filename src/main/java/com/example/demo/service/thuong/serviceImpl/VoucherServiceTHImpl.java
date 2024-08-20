package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.entity.Customer;
import com.example.demo.entity.CustomerType;
import com.example.demo.entity.Voucher;
import com.example.demo.model.response.thuong.VoucherResponseTH;
import com.example.demo.repository.thuong.BillRepositoryTH;
import com.example.demo.repository.thuong.CustomerRepositoryTH;
import com.example.demo.repository.thuong.CustomerTypeVouchersRepositoryTH;
import com.example.demo.repository.thuong.VoucherRepositoryTH;
import com.example.demo.service.thuong.VoucherServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoucherServiceTHImpl implements VoucherServiceTH {


    @Autowired
    private VoucherRepositoryTH voucherCommonRepository;

    @Autowired
    private CustomerTypeVouchersRepositoryTH customerTypeVouchersRepository;

    @Autowired
    private CustomerRepositoryTH customerRepository;

    @Autowired
    private BillRepositoryTH billRepository;

    public  Integer checkNumberOfUser(Customer customer,Voucher voucher){
        Integer countUse = billRepository.countVoucherUsageByCustomer(customer.getId(),voucher.getId());
//        System.out.println(countUse);
//        System.out.println(voucher.getNumberOfUses());
        if (countUse > voucher.getNumberOfUses()){
            return 2;
        }
        return 1;
    }

    @Override
    public List<VoucherResponseTH> getVouchersForCustomer(Long id, String search) {
        // Nếu id là null, trả về các voucher có trạng thái 1 và áp dụng cho tất cả
        if (id == null) {
            List<Voucher> voucherStatus0 = voucherCommonRepository.findAllByStatus1AndApplyFor();

            // Filter by voucher name or code
            if (search != null && !search.isEmpty()) {
                voucherStatus0 = voucherStatus0.stream()
                        .filter(voucher -> voucher.getName().toLowerCase().contains(search.toLowerCase())
                                || voucher.getCode().toLowerCase().contains(search.toLowerCase()))
                        .collect(Collectors.toList());
            }

            // Sort by maximumReductionValue in descending order
            voucherStatus0.sort(Comparator.comparing(Voucher::getMaximumReductionValue).reversed());

            // Convert List<Voucher> to List<VoucherDTO>
            return voucherStatus0.stream()
                    .map(voucher -> new VoucherResponseTH(
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
                            1 // Mặc định là 1 vì không có khách hàng cụ thể để kiểm tra số lượng sử dụng
                    ))
                    .collect(Collectors.toList());
        }

        // Fetch the customer
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {
            CustomerType customerType = customer.get().getCustomerType();
            List<Voucher> vouchersFromCustomerType = new ArrayList<>();

            if (customerType != null && customerType.getId() != null) {
                // Criterion 1: get vouchers by CustomerTypeVouchers
                List<Long> voucherIdsFromCustomerType = customerTypeVouchersRepository.findVoucherIdsByCustomerTypeId(customerType.getId());
                vouchersFromCustomerType = voucherCommonRepository.findAllByIdAndStatus(voucherIdsFromCustomerType);
            }

            // Criterion 2: get vouchers by apply for all
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
            List<VoucherResponseTH> voucherDTOs = combinedVouchers.stream()
                    .map(voucher -> new VoucherResponseTH(
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
                            // 1 hiển thị được, 2 vượt quá số lượng giới hạn voucher
                            checkNumberOfUser(customer.get(), voucher)
                    ))
                    .collect(Collectors.toList());

            return voucherDTOs;
        }

        return null;
    }
}
