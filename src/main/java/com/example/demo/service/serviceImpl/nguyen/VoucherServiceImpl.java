package com.example.demo.service.serviceImpl.nguyen;

import com.example.demo.entity.Voucher;
import com.example.demo.repository.nguyen.VoucherRepository;
import com.example.demo.repository.nguyen.VoucherSpecification;
import com.example.demo.service.nguyen.VoucherService;
//import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    VoucherRepository voucherRepository;

    @Override
    public List<Voucher> getAllVoucher() {
        updateStatus();

        return voucherRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Voucher getVoucherById(Long id) {
        updateStatus();

        return voucherRepository.findById(id).get();
    }

    @Override
    public Voucher createVoucher(Voucher voucher) {
        voucher.setNumberOfUses(1);
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
            v.setCode(voucher.getCode());
            v.setName(voucher.getName());
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
        if (currentDate.after(startDate) && currentDate.before(endDate)) return 1;
        if (currentDate.after(endDate)) return 2;
        if (currentDate.before(startDate)) return 3;
        return null;
    }

    public void updateStatus() {
        for (Voucher v : voucherRepository.findAll()) {
            if (v.getStartDate() != null && v.getEndDate() != null) {
                if (v.getStatus() != checkDate(v.getStartDate(), v.getEndDate())) {
                    v.setStatus(checkDate(v.getStartDate(), v.getEndDate()));
                    voucherRepository.save(v);
                }
            }
        }
    }

    @Override
    public Page<Voucher> findVouchers(String name, String code, Integer discountType, Date startDate, Date endDate, Integer status, Pageable pageable) {
        Specification<Voucher> spec = Specification.where(VoucherSpecification.hasName(name))
                .and(VoucherSpecification.hasCode(code))
                .and(VoucherSpecification.hasDiscountType(discountType))
                .and(VoucherSpecification.hasStartDate(startDate))
                .and(VoucherSpecification.hasEndDate(endDate))
                .and(VoucherSpecification.hasStatus(status));
        return voucherRepository.findAll(spec, pageable);
    }
}
