package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.entity.BillHistory;
import com.example.demo.entity.PaymentStatus;
import com.example.demo.model.request.nguyen.PaymentStatusRequest;
import com.example.demo.repository.nguyen.bill.NBillDetailRepository;
import com.example.demo.repository.nguyen.bill.NBillHistoryRepository;
import com.example.demo.repository.nguyen.bill.NBillRepository;
import com.example.demo.repository.nguyen.bill.NPaymentStatusRepository;
import com.example.demo.service.nguyen.NPaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class NPaymentStatusServiceImpl implements NPaymentStatusService {

    @Autowired
    NPaymentStatusRepository paymentStatusRepository;

    @Autowired
    NBillRepository billRepository;

    @Autowired
    NBillHistoryRepository billHistoryRepository;

    @Autowired
    NBillDetailRepository billDetailRepository;

    @Override
    public List<PaymentStatus> getAllByBillId(Long billId) {
        return paymentStatusRepository.findAllByBillIdOrderByIdAsc(billId);
    }

    @Override
    @Transactional
    public PaymentStatus createPaymentStatus(Long billId,
                                             PaymentStatusRequest paymentStatusRequest) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        if (paymentStatusRequest.getPayOrRefund() == 1) {
            bill.setPaidAmount(paymentStatusRequest.getBill().getTotalAmountAfterDiscount());
            bill.setPaidShippingFee(paymentStatusRequest.getBill().getShippingFee());

            billRepository.save(bill);

//            setReasonBillHistory(bill, 21);
        } else if (paymentStatusRequest.getPayOrRefund() == 2) {
            bill.setPaidAmount(paymentStatusRequest.getBill().getTotalAmountAfterDiscount());
            bill.setPaidShippingFee(paymentStatusRequest.getBill().getShippingFee());

            billRepository.save(bill);

            setReasonBillHistory(bill, 21);
        } else if (paymentStatusRequest.getPayOrRefund() == 3) {
            bill.setPaidAmount(BigDecimal.ZERO);
            bill.setPaidShippingFee(BigDecimal.ZERO);

            billRepository.save(bill);

            setReasonBillHistory(bill, 22);
        } else if (paymentStatusRequest.getPayOrRefund() == 0) {
            bill.setPaidAmount(paymentStatusRequest.getBill().getTotalAmountAfterDiscount());
            bill.setPaidShippingFee(paymentStatusRequest.getBill().getShippingFee());

            billRepository.save(bill);
        } else if(paymentStatusRequest.getPayOrRefund() == 10) {
            // thêm chữ đã hoàn tiền
            setReasonBillHistory(bill, 22);
        }

        PaymentStatus ps = new PaymentStatus();
        ps.setCode(generateUniqueCode());
        ps.setPaymentDate(new Date());
        ps.setBill(bill);
        ps.setPaymentAmount(paymentStatusRequest.getPaymentStatus().getPaymentAmount());
        ps.setPaymentType(paymentStatusRequest.getPaymentStatus().getPaymentType());
        ps.setPaymentMethod(paymentStatusRequest.getPaymentStatus().getPaymentMethod());
        ps.setNote(paymentStatusRequest.getPaymentStatus().getNote());
        ps.setCustomerPaymentStatus(
                paymentStatusRequest.getPaymentStatus().getCustomerPaymentStatus());

        return paymentStatusRepository.save(ps);
    }

    @Override
    public Integer checkIsRefund(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

//        if(bill.getStatus() != 32) return 0;

        List<PaymentStatus> paymentStatuses = paymentStatusRepository.findAllByBillIdAndPaymentTypeAndBillStatus(id, 4, 32);

        if(paymentStatuses.isEmpty()) return 0;

        return 1; //nếu đã hoàn
    }

    private void setReasonBillHistory(Bill bill, Integer reason) {
        BillHistory billHistory = billHistoryRepository
                .findAllByBillIdAndStatus(bill.getId(), bill.getStatus());
        if (bill.getStatus() == 32) {
            billHistory.setReason(reason);
        } else if (bill.getStatus() == 4){
            billHistory.setReason(reason);
        }
        billHistoryRepository.save(billHistory);
    }


    @Override
    @Transactional
    public PaymentStatus updateStatusPayment(Long billId, String note) {

        List<PaymentStatus> paymentStatuses = paymentStatusRepository
                .findAllByBillIdAndCustomerPaymentStatusOrderByIdAsc(billId, 1);

        if (paymentStatuses.isEmpty()) return null;

        for (PaymentStatus ps : paymentStatuses) {
            if (ps.getCustomerPaymentStatus() == 1 && ps.getPaymentType() == 1) {
                ps.setPaymentDate(new Date());
                ps.setCustomerPaymentStatus(2);

                return paymentStatusRepository.save(ps);
            }
        }
        return null;
    }

    @Override
    @Transactional
    public PaymentStatus updateStatusPaymentRefund(Long billId, String note) {

        List<PaymentStatus> paymentStatuses = paymentStatusRepository
                .findAllByBillIdAndCustomerPaymentStatusOrderByIdAsc(billId, 4);

        if (paymentStatuses.isEmpty()) return null;

        for (PaymentStatus ps : paymentStatuses) {
            if (ps.getCustomerPaymentStatus() == 4 && ps.getPaymentType() == 3) {
                ps.setPaymentDate(new Date());
                ps.setCustomerPaymentStatus(3);

                return paymentStatusRepository.save(ps);
            }
        }
        return null;
    }

    private static final Random random = new Random();
    private static final String PREFIX = "TT";
    private static final int MAX_ATTEMPTS = 1000;

    public String generateUniqueCode() {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            int randomNumber = random.nextInt(10000);
            String code = PREFIX + String.format("%04d", randomNumber);

            if (!paymentStatusRepository.existsByCode(code)) {
                return code;
            }
        }
        throw new RuntimeException("Không thể tạo mã duy nhất sau " + MAX_ATTEMPTS + " lần thử.");
    }
}
