package com.example.demo.service.common.impl;

import com.example.demo.repository.common.BillRepositoryCommonRepository;
import com.example.demo.service.common.BillCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service

public class BillCommonServiceImpl implements BillCommonService {


    @Autowired
    private BillRepositoryCommonRepository billRepositoryCommonRepository;


    @Override
    public Integer countVoucherUsageByCustomer(Long customerId, Long voucherId) {
        return billRepositoryCommonRepository.countVoucherUsageByCustomer(customerId, voucherId);
    }

}
