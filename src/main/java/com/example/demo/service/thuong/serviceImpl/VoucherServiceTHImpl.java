package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.entity.*;
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

    public List<Voucher> getVouchersForCustomer(Customer customer) {
        if (customer.getCustomerType() == null) {

            return new ArrayList<>();
        }

        List<CustomerTypeVoucher> customerTypeVouchers = customerTypeVouchersRepository
                .findByCustomerTypeId(customer.getCustomerType().getId());
        List<Voucher> filteredVouchers = customerTypeVouchers.stream()
                .map(CustomerTypeVoucher::getVoucher)
                .filter(voucher -> voucher.getApplyfor() == 1 && voucher.getStatus() == 1)
                .collect(Collectors.toList());

        return filteredVouchers;
    }






    public  Integer checkNumberOfUser(Customer customer,Voucher voucher){
        if (voucher.getApplyfor() == 0){
            return 1;
        }
        Integer countUse = billRepository.countVoucherUsageByCustomer(customer.getId(),voucher.getId());
        if (countUse >= voucher.getNumberOfUses()){
            return 2;
        }
        return 1;
    }

    @Override
    public List<VoucherResponseTH> getVouchersForCustomer(Long id, String search) {
        List<Voucher> combinedVouchers = new ArrayList<>();
        Optional<Customer> customerOpt = null;
        // Trường hợp id là null: chỉ lấy các voucher có trạng thái 1 và áp dụng cho tất cả
        if (id == null) {

            combinedVouchers = voucherCommonRepository.findAllByStatus1AndApplyFor();
        } else {
            // Fetch the customer
            customerOpt = customerRepository.findById(id);

            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                CustomerType customerType = customer.getCustomerType();

                // Lấy các voucher từ CustomerType nếu customerType không null
                if (customerType != null && customerType.getId() != null) {
                    combinedVouchers.addAll(getVouchersForCustomer(customer));

                }

                // Lấy thêm các voucher có trạng thái 1 và áp dụng cho tất cả
                combinedVouchers.addAll(voucherCommonRepository.findAllByStatus1AndApplyFor());

            }
        }


        // Loại bỏ các voucher trùng lặp

        // Filter by voucher name or code
        if (search != null && !search.isEmpty()) {
            combinedVouchers = combinedVouchers.stream()
                    .filter(voucher -> voucher.getName().toLowerCase().contains(search.toLowerCase())
                            || voucher.getCode().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Sort by maximumReductionValue in descending order
        combinedVouchers.sort(Comparator.comparing(Voucher::getMaximumReductionValue).reversed());

        // Convert List<Voucher> to List<VoucherResponseTH>
        Optional<Customer> finalCustomerOpt = customerOpt;
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
                        (id == null) ? 1 : checkNumberOfUser(finalCustomerOpt.get(), voucher) // Mặc định là 1 nếu id là null
                ))
                .collect(Collectors.toList());
        return voucherDTOs;
    }
}
