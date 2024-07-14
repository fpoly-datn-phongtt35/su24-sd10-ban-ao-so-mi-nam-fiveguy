package com.example.demo.service.common.impl;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Voucher;
import com.example.demo.repository.common.CustomerRepositoryCommon;
import com.example.demo.repository.common.OLCustomerTypeVouchersRepository2;
import com.example.demo.repository.common.OLVoucherRepository2;
import com.example.demo.service.common.OLVoucherService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class OLVoucherServiceImpl2 implements OLVoucherService2 {

    @Autowired
    private OLVoucherRepository2 olVoucherRepository2;

    @Autowired
    private OLCustomerTypeVouchersRepository2 customerTypeVouchersRepository;

    @Autowired
    private CustomerRepositoryCommon customerRepositoryCommon;


    @Override
    public List<Voucher> getAllVouchersByStatusAndApplyFor() {
        return olVoucherRepository2.findAllByStatusAndApplyFor();
    }



    @Override
    public List<Voucher> getVouchersForCustomer(Customer customer) {
        // Fetch the customer

        // Criterion 1: get vouchers by CustomerTypeVouchers
        List<Long> voucherIdsFromCustomerType = customerTypeVouchersRepository.findVoucherIdsByCustomerTypeId(customer.getCustomerType().getId());
        List<Voucher> vouchersFromCustomerType = olVoucherRepository2.findAllByIdAndStatus(voucherIdsFromCustomerType);


        // Criterion 2: get vouchers by applyfor all
        List<Voucher> voucherStatus0 = olVoucherRepository2.findAllByStatus1AndApplyFor();


        // Combine both lists
        List<Voucher> combinedVouchers = vouchersFromCustomerType;
        combinedVouchers.addAll(voucherStatus0);

        // Remove duplicates
        combinedVouchers = combinedVouchers.stream().distinct().collect(Collectors.toList());

        return combinedVouchers;
    }

    @Override
    public List<Voucher> getVouchersForCustomer(Long id) {
        // Fetch the customer
        Optional<Customer> customer = customerRepositoryCommon.findById(id);

        if (customer.isPresent()){
            // Criterion 1: get vouchers by CustomerTypeVouchers
            List<Long> voucherIdsFromCustomerType = customerTypeVouchersRepository.findVoucherIdsByCustomerTypeId(customer.get().getCustomerType().getId());
            List<Voucher> vouchersFromCustomerType = olVoucherRepository2.findAllByIdAndStatus(voucherIdsFromCustomerType);


            // Criterion 2: get vouchers by applyfor all
            List<Voucher> voucherStatus0 = olVoucherRepository2.findAllByStatus1AndApplyFor();


            // Combine both lists
            List<Voucher> combinedVouchers = vouchersFromCustomerType;
            combinedVouchers.addAll(voucherStatus0);

            // Remove duplicates
            combinedVouchers = combinedVouchers.stream().distinct().collect(Collectors.toList());

            return combinedVouchers;
        }
        return null;

    }

    @Override
    public Voucher selectBestVoucher(double totalAmount,Customer customer) {
        List<Voucher> vouchers = getVouchersForCustomer(customer);
        Voucher bestVoucher = null;
        double minDifference = Double.POSITIVE_INFINITY;

        for (Voucher voucher : vouchers) {
            if (voucher.getQuantity() > 0 && totalAmount >= voucher.getMinimumTotalAmount()) {
                double difference = totalAmount - voucher.getMinimumTotalAmount();
                if (difference < minDifference) {
                    minDifference = difference;
                    bestVoucher = voucher;
                }
            }
        }

        return bestVoucher;
    }


}
