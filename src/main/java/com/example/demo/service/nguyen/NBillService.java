package com.example.demo.service.nguyen;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.entity.BillHistory;
import com.example.demo.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface NBillService {

    List<Bill> getAll();

    Bill getById(Long id);

    List<Bill> searchBills(String code, String customerName, String phoneNumber,
                           Integer typeBill, Date createdAt, Integer status);

    Page<Bill> searchBills(String code, String customerName, String phoneNumber,
                           Integer typeBill, Date startDate, Date endDate,
                           Integer status, Pageable pageable);

    Bill updateStatusAndBillStatus(Bill bill, Long id, BillHistory billHistory);

    Bill updateShipmentDetail(Bill bill, Long id, String fullName);

    Integer isQuantityExceedsProductDetail(Long idBill);

    Bill setVoucherToBill(Long id, Voucher voucher);

    Bill updateShippingFee(Long id, BigDecimal shippingFee);
}
