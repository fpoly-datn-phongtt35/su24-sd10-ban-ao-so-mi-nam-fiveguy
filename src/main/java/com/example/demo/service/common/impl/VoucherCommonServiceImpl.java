package com.example.demo.service.common.impl;

import com.example.demo.entity.Customer;
import com.example.demo.entity.CustomerType;
import com.example.demo.entity.CustomerTypeVoucher;
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

    public List<Voucher> getVouchersForCustomer(Customer customer) {
        List<Voucher> applicableVouchers = new ArrayList<>();
        List<CustomerTypeVoucher> customerTypeVouchers = customerTypeVouchersRepository
                .findByCustomerTypeId(customer.getCustomerType().getId());
        applicableVouchers = customerTypeVouchers.stream()
                .map(CustomerTypeVoucher::getVoucher)
                .filter(voucher -> voucher.getApplyfor() == 1 && voucher.getStatus() == 1)
                .collect(Collectors.toList());

        return applicableVouchers;
    }



    @Override
    public List<VoucherDTO> getVouchersForCustomer(Customer customer, String search) {
        List<VoucherDTO> voucherDTOs = new ArrayList<>();

        // Criterion 1: Get vouchers by CustomerTypeVouchers, if the customer has a CustomerType
        if (customer.getCustomerType() != null) {
            List<Voucher> vouchersFromCustomerType = getVouchersForCustomer(customer);

            // Convert to VoucherDTO and apply checkNumberOfUser
            voucherDTOs.addAll(vouchersFromCustomerType.stream()
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
                            checkNumberOfUser(customer, voucher) ,
                            voucher.getNumberOfUses(),
                            getNumberOfUser(customer, voucher),
                            voucher.getApplyfor()

                            ))
                    .collect(Collectors.toList()));
        }

        // Criterion 2: Get vouchers by apply for all
        List<Voucher> voucherStatus0 = voucherCommonRepository.findAllByStatus1AndApplyFor();

        // Convert to VoucherDTO and set checkNumberOfUser to 1
        voucherDTOs.addAll(voucherStatus0.stream()
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
                        1,
                        1,
                        1,
                        voucher.getApplyfor()
                ))
                .collect(Collectors.toList()));

        // Remove duplicates
        voucherDTOs = voucherDTOs.stream().distinct().collect(Collectors.toList());

        // Filter by voucher name or code
        if (search != null && !search.isEmpty()) {
            voucherDTOs = voucherDTOs.stream()
                    .filter(voucher -> voucher.getName().toLowerCase().contains(search.toLowerCase())
                            || voucher.getCode().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Sort by maximumReductionValue in descending order
        voucherDTOs.sort(Comparator.comparing(VoucherDTO::getMaximumReductionValue).reversed());

        return voucherDTOs;
    }



    public  Integer checkNumberOfUser(Customer customer,Voucher voucher){
           Integer countUse = billServiceCommonImpl.countVoucherUsageByCustomer(customer.getId(),voucher.getId());
           if (countUse >= voucher.getNumberOfUses()){
               return 2;
           }
           return 1;
        }

    public  Integer getNumberOfUser(Customer customer,Voucher voucher){
        Integer countUse = billServiceCommonImpl.countVoucherUsageByCustomer(customer.getId(),voucher.getId());

        return countUse;
    }


//        Lấy list voucher cho bán tại quầy truyền tạo controller chọn khách hàng có id truyền vào
//    ----
//    ----
//    ----
@Override
public List<VoucherDTO> getVouchersForCustomer(Long id, String search) {


    return null;
}




}
