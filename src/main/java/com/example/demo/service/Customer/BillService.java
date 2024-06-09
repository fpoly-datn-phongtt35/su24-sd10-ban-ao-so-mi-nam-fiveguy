package com.example.demo.service.Customer;

import com.example.demo.entity.Bill;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BillService {
    List<Bill> getAllBill();

    Bill getBillById(Long id);

    Page<Bill> getAllBillPage(Integer page);

    Bill createBill(Bill bill);

    Bill updateBill(Bill bill, Long id);

    void deleteBill(Long id);


    Bill updateStatus(Integer status, Long id);

    List<Bill> getAllExportExcel();
// //OL

//    ResponseEntity<?> TaoHoaDonNguoiDungChuaDangNhap(@RequestBody JsonNode orderData);

    Page<Bill> findLatestBillsByCustomerId(Long customerId, int page, int size);

    boolean updatePaymentStatus(Long billId, int paymentStatus);

//    BillResponse findBYId(Long id);

    Bill save(Bill bill);

    Bill findById(Long id);

    List<Bill> findByPhoneNumber(String pn);

// // END OL
}
