package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.*;
import com.example.demo.model.response.nguyen.ReturnOrderSummary;
import com.example.demo.repository.nguyen.bill.*;
import com.example.demo.repository.nguyen.product.NProductDetailRepository;
import com.example.demo.service.nguyen.NBillService;
import com.example.demo.service.nguyen.NReturnOrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NReturnOrderServiceImpl implements NReturnOrderService {

    @Autowired
    NReturnOrderRepository returnOrderRepository;

    @Autowired
    NBillRepository billRepository;

    @Autowired
    NBillDetailRepository billDetailRepository;

    @Autowired
    NProductDetailRepository productDetailRepository;

    @Autowired
    NBillHistoryRepository billHistoryRepository;

    @Autowired
    NPaymentStatusRepository paymentStatusRepository;

    @Override
    public List<ReturnOrder> findAllReturnOrdersByBillId(Long billId) {
        return returnOrderRepository.findAllReturnOrdersByBillIdOrderByCreatedAtDesc(billId);
    }

    @Override
    @Transactional
    public List<ReturnOrder> addReturnOrderAndUpdateBill(List<ReturnOrder> returnOrders, String fullName) {
        if (returnOrders.isEmpty()) {
            throw new IllegalArgumentException("Danh sách ReturnOrder không được rỗng");
        }

        Bill bill = billRepository.findById(returnOrders.get(0).getBill().getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Bill"));

        ReturnOrderSummary returnOrderSummary = calculateReturnOrderSummary(bill.getId(), returnOrders);

        List<ReturnOrder> savedReturnOrders = returnOrderRepository.saveAll(returnOrders);

        updateBillDetailsQuantities(savedReturnOrders);

//        PaymentStatus newPaymentStatus = createNewPaymentStatus(bill, returnOrderSummary.getTongTienTra());
//        paymentStatusRepository.save(newPaymentStatus);

        updateBill(bill, returnOrderSummary);

        BillHistory newBillHistory = createNewBillHistory(bill, fullName);
        billHistoryRepository.save(newBillHistory);

        return savedReturnOrders;
    }

    private PaymentStatus createNewPaymentStatus(Bill bill, BigDecimal refundAmount) {
        PaymentStatus newPaymentStatus = new PaymentStatus();
        newPaymentStatus.setBill(bill);
        newPaymentStatus.setCustomerPaymentStatus(0);
        newPaymentStatus.setPaymentAmount(refundAmount);
        newPaymentStatus.setPaymentDate(new Date());
        newPaymentStatus.setCode("auto");
        newPaymentStatus.setNote("Hoàn tiền cho khách");
        return newPaymentStatus;
    }

    private void updateBill(Bill bill, ReturnOrderSummary summary) {
        bill.setTotalAmount(summary.getTongTienSauKhiTra());
        bill.setTotalAmountAfterDiscount(summary.getTongTienDaGiamSauKhiTra());
        bill.setStatus(30);  // Đảm bảo 23 là trạng thái đúng
        billRepository.save(bill);
    }

    private BillHistory createNewBillHistory(Bill bill, String fullName) {
        BillHistory newBillHistory = new BillHistory();
        newBillHistory.setBill(bill);
        newBillHistory.setCreatedAt(new Date());
        newBillHistory.setReason(0);
        newBillHistory.setType(1);
        newBillHistory.setCreatedBy(fullName);
        newBillHistory.setStatus(30);  // Đảm bảo 23 là trạng thái đúng
        newBillHistory.setDescription("Trả hàng");
        return newBillHistory;
    }

    @Transactional
    public List<ReturnOrder> addReturnOrderAndUpdateBill1(List<ReturnOrder> returnOrders,
                                                   String fullName) {
        if (returnOrders.isEmpty()) {
            throw new IllegalArgumentException("Danh sách ReturnOrder không được rỗng");
        }

        Optional<Bill> bill = billRepository.findById(returnOrders.get(0).getBill().getId());

        if (bill.isEmpty()) return null;

        ReturnOrderSummary returnOrderSummary = calculateRefundSummary(bill.get(), returnOrders);

        List<ReturnOrder> returnRO = returnOrderRepository.saveAll(returnOrders);

        //Trừ số lượng trong billDetail
        updateBillDetailsQuantities(returnRO);

        PaymentStatus newPaymentStatus = new PaymentStatus();
        newPaymentStatus.setBill(bill.get());
        newPaymentStatus.setCustomerPaymentStatus(0);
        //set tổng tiền trả cho khách
        newPaymentStatus.setPaymentAmount(returnOrderSummary.getTongTienTra());
        newPaymentStatus.setPaymentDate(new Date());
        newPaymentStatus.setCode("auto");
        newPaymentStatus.setNote("Hoàn tiền cho khách");

        paymentStatusRepository.save(newPaymentStatus);

        Bill newBill = new Bill();
        newBill.setTotalAmount(returnOrderSummary.getTongTienSauKhiTra());
        newBill.setTotalAmountAfterDiscount(returnOrderSummary.getTongTienDaGiamSauKhiTra());
//        newBill.setVoucher();
        newBill.setStatus(30);

        billRepository.save(newBill);

        BillHistory newBillHistory = new BillHistory();
        newBillHistory.setBill(bill.get());
        newBillHistory.setCreatedAt(new Date());
        newBillHistory.setReason(0);
        newBillHistory.setType(1);
        newBillHistory.setCreatedBy(fullName);
        newBillHistory.setStatus(30);
        newBillHistory.setDescription("Trả hàng");

        billHistoryRepository.save(newBillHistory);

        return returnRO;
    }

    @Override
    public ReturnOrderSummary calculateRefundSummary(Bill bill,
                                                            List<ReturnOrder> returnOrders) {
        ReturnOrderSummary summary = new ReturnOrderSummary();

        // Tổng giá trị đơn hàng trước khi giảm giá
        BigDecimal totalAmount = bill.getTotalAmount();
        summary.setTongTien(totalAmount);

        // Tổng giá trị đơn hàng sau khi giảm giá
        BigDecimal totalAmountAfterDiscount = bill.getTotalAmountAfterDiscount();
        summary.setTongTienDaGiam(totalAmountAfterDiscount);

        // Tỷ lệ giảm giá
        BigDecimal discountRate = totalAmountAfterDiscount
                .divide(totalAmount, RoundingMode.HALF_UP);
        summary.setTiLeGiam(discountRate);

        // Tổng số lượng sản phẩm trong đơn hàng
        int totalQuantity = bill.getBillDetail().stream().mapToInt(BillDetail::getQuantity).sum();
        summary.setTongSoLuong(totalQuantity);

        // Giá trị trung bình mỗi sản phẩm trước và sau khi giảm giá
        BigDecimal averageProductPrice = totalAmount
                .divide(BigDecimal.valueOf(totalQuantity), RoundingMode.HALF_UP);
        summary.setGiaTrungBinhSanPham(averageProductPrice);

        BigDecimal averageProductPriceAfterDiscount = totalAmountAfterDiscount
                .divide(BigDecimal.valueOf(totalQuantity), RoundingMode.HALF_UP);
        summary.setGiaTrungBinhSanPhamDaGiam(averageProductPriceAfterDiscount);

        // Tổng số lượng sản phẩm trả hàng
        int totalReturnQuantity = returnOrders.stream().mapToInt(ReturnOrder::getQuantity).sum();
        summary.setTongSoLuongTraLai(totalReturnQuantity);

        // Tổng số tiền phải trả lại
        BigDecimal totalRefundAmount = averageProductPriceAfterDiscount
                .multiply(BigDecimal.valueOf(totalReturnQuantity));
        summary.setTongTienTra(totalRefundAmount);

        // Tổng giá trị đơn hàng sau khi trả
        BigDecimal totalAmountAfterRefund = totalAmount.subtract(totalRefundAmount);
        summary.setTongTienSauKhiTra(totalAmountAfterRefund);

        // Tổng giá trị đơn hàng đã giảm sau khi trả
        BigDecimal totalAmountAfterDiscountAfterRefund = totalAmountAfterDiscount
                .subtract(totalRefundAmount);
        summary.setTongTienDaGiamSauKhiTra(totalAmountAfterDiscountAfterRefund);

        return summary;
    }

    //Trừ số lượng trong billDetail khi hoàn trả
    public void updateBillDetailsQuantities(List<ReturnOrder> returnOrders) {
        for (ReturnOrder returnOrder : returnOrders) {
            BillDetail billDetail = returnOrder.getBillDetail();
            billDetail.setQuantity(billDetail.getQuantity() - returnOrder.getQuantity());
            billDetailRepository.save(billDetail);
        }
    }



    @Transactional
    public ReturnOrder createReturnOrder(Long billId, Long billDetailId, int quantity, String reason) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        BillDetail billDetail = bill.getBillDetail().stream()
                .filter(bd -> bd.getId().equals(billDetailId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("BillDetail not found"));

        if (quantity > billDetail.getQuantity()) {
            throw new IllegalArgumentException("Return quantity exceeds original quantity");
        }

        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setBill(bill);
        returnOrder.setBillDetail(billDetail);
        returnOrder.setQuantity(quantity);
        returnOrder.setReturnReason(reason);
        returnOrder.setReturnStatus(0); // Assuming 0 is the initial status
        returnOrder.setCreatedAt(new Date());

        return returnOrderRepository.save(returnOrder);
    }

    public ReturnOrderSummary calculateReturnOrderSummary(Long billId, List<ReturnOrder> returnOrders) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        if (returnOrders.isEmpty()) return null;

        ReturnOrderSummary summary = new ReturnOrderSummary();

        BigDecimal tiLeGiam = BigDecimal.ONE.subtract(
                bill.getTotalAmountAfterDiscount().divide(bill.getTotalAmount(), 4, RoundingMode.HALF_UP)
        );

        BigDecimal tongTienTra = BigDecimal.ZERO;
        int tongSoLuongTraLai = 0;

        for (ReturnOrder returnOrder : returnOrders) {
            BigDecimal giaGoc = returnOrder.getBillDetail().getPromotionalPrice();
            int soLuong = returnOrder.getQuantity();

            BigDecimal tienHoanTra = giaGoc.multiply(BigDecimal.valueOf(soLuong))
                    .multiply(BigDecimal.ONE.subtract(tiLeGiam)).setScale(0, RoundingMode.HALF_UP);

            tongTienTra = tongTienTra.add(tienHoanTra);
            tongSoLuongTraLai += soLuong;
        }

        summary.setTongTien(bill.getTotalAmount().setScale(0, RoundingMode.HALF_UP));
        summary.setTongTienDaGiam(bill.getTotalAmountAfterDiscount().setScale(0, RoundingMode.HALF_UP));
        summary.setTongTienSauKhiTra(bill.getTotalAmount().subtract(tongTienTra).setScale(0, RoundingMode.HALF_UP));
        summary.setTongTienDaGiamSauKhiTra(bill.getTotalAmountAfterDiscount().subtract(tongTienTra).setScale(0, RoundingMode.HALF_UP));
        summary.setTiLeGiam(tiLeGiam.setScale(4, RoundingMode.HALF_UP));
        summary.setTongSoLuong(bill.getBillDetail().stream().mapToInt(BillDetail::getQuantity).sum());
        summary.setTongSoLuongTraLai(tongSoLuongTraLai);
        summary.setGiaTrungBinhSanPham(bill.getTotalAmount()
                .divide(BigDecimal.valueOf(summary.getTongSoLuong()), 0, RoundingMode.HALF_UP));
        summary.setGiaTrungBinhSanPhamDaGiam(bill.getTotalAmountAfterDiscount()
                .divide(BigDecimal.valueOf(summary.getTongSoLuong()), 0, RoundingMode.HALF_UP));
        summary.setTongTienTra(tongTienTra.setScale(0, RoundingMode.HALF_UP));

        return summary;
    }

    public List<ReturnOrder> getReturnOrdersByBillId(Long billId) {
        return returnOrderRepository.findByBillId(billId);
    }

    @Transactional
    public ReturnOrder updateReturnOrderStatus(Long returnOrderId, int newStatus) {
        ReturnOrder returnOrder = returnOrderRepository.findById(returnOrderId)
                .orElseThrow(() -> new RuntimeException("Return order not found"));

        returnOrder.setReturnStatus(newStatus);
        returnOrder.setUpdatedAt(new Date());

        return returnOrderRepository.save(returnOrder);
    }

    public BigDecimal calculateTotalRefundAmount(Long billId, List<ReturnOrder> returnOrders) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        BigDecimal tiLeGiam = BigDecimal.ONE.subtract(
                bill.getTotalAmountAfterDiscount().divide(bill.getTotalAmount(), 4, RoundingMode.HALF_UP)
        );

        return returnOrders.stream()
                .map(ro -> {
                    BigDecimal giaGoc = ro.getBillDetail().getPromotionalPrice();
                    int soLuong = ro.getQuantity();
                    return giaGoc.multiply(BigDecimal.valueOf(soLuong))
                            .multiply(BigDecimal.ONE.subtract(tiLeGiam));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }



    //Tính đúng nhưng dài
//    @Transactional
//    public ReturnOrder createReturnOrder(Long billId, Long billDetailId, int quantity, String reason) {
//        Bill bill = billRepository.findById(billId)
//                .orElseThrow(() -> new RuntimeException("Bill not found"));
//
//        BillDetail billDetail = bill.getBillDetail().stream()
//                .filter(bd -> bd.getId().equals(billDetailId))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("BillDetail not found"));
//
//        if (quantity > billDetail.getQuantity()) {
//            throw new IllegalArgumentException("Return quantity exceeds original quantity");
//        }
//
//        ReturnOrder returnOrder = new ReturnOrder();
//        returnOrder.setBill(bill);
//        returnOrder.setBillDetail(billDetail);
//        returnOrder.setQuantity(quantity);
//        returnOrder.setReturnReason(reason);
//        returnOrder.setReturnStatus(0); // Assuming 0 is the initial status
//        returnOrder.setCreatedAt(new Date());
//
//        return returnOrderRepository.save(returnOrder);
//    }
//
//
//    public ReturnOrderSummary calculateReturnOrderSummary(Long billId, List<ReturnOrder> returnOrders) {
//        Bill bill = billRepository.findById(billId)
//                .orElseThrow(() -> new RuntimeException("Bill not found"));
//
////        List<ReturnOrder> returnOrders = bill.getReturnOrders();
//
//        ReturnOrderSummary summary = new ReturnOrderSummary();
//
//        BigDecimal totalRefundAmount = BigDecimal.ZERO;
//        int totalReturnQuantity = 0;
//
//        for (ReturnOrder returnOrder : returnOrders) {
//            BillDetail billDetail = returnOrder.getBillDetail();
//            BigDecimal refundAmount = calculateRefundAmount(bill, billDetail, returnOrder);
//            totalRefundAmount = totalRefundAmount.add(refundAmount);
//            totalReturnQuantity += returnOrder.getQuantity();
//        }
//
//        summary.setTongTien(bill.getTotalAmount());
//        summary.setTongTienDaGiam(bill.getTotalAmountAfterDiscount());
//        summary.setTongTienSauKhiTra(bill.getTotalAmount().subtract(totalRefundAmount));
//        summary.setTongTienDaGiamSauKhiTra(bill.getTotalAmountAfterDiscount().subtract(totalRefundAmount));
//        summary.setTiLeGiam(BigDecimal.ONE.subtract(bill.getTotalAmountAfterDiscount().divide(bill.getTotalAmount(), 4, RoundingMode.HALF_UP)));
//        summary.setTongSoLuong(bill.getBillDetail().stream().mapToInt(BillDetail::getQuantity).sum());
//        summary.setTongSoLuongTraLai(totalReturnQuantity);
//        summary.setGiaTrungBinhSanPham(bill.getTotalAmount().divide(BigDecimal.valueOf(summary.getTongSoLuong()), 2, RoundingMode.HALF_UP));
//        summary.setGiaTrungBinhSanPhamDaGiam(bill.getTotalAmountAfterDiscount().divide(BigDecimal.valueOf(summary.getTongSoLuong()), 2, RoundingMode.HALF_UP));
//        summary.setTongTienTra(totalRefundAmount);
//
//        return summary;
//    }
//
//
//    private BigDecimal calculateRefundAmount(Bill bill, BillDetail billDetail, ReturnOrder returnOrder) {
//        BigDecimal totalBillAmount = bill.getTotalAmount();
//        BigDecimal totalDiscountAmount = bill.getTotalAmount().subtract(bill.getTotalAmountAfterDiscount());
//
//        BigDecimal originalItemPrice = billDetail.getPromotionalPrice();
//        int returnQuantity = returnOrder.getQuantity();
//
//        // Tính tỷ lệ giá trị của sản phẩm này so với tổng giá trị đơn hàng
//        BigDecimal itemValueRatio = originalItemPrice.multiply(BigDecimal.valueOf(billDetail.getQuantity()))
//                .divide(totalBillAmount, 4, RoundingMode.HALF_UP);
//
//        // Tính số tiền giảm giá cho sản phẩm này
//        BigDecimal itemDiscountAmount = totalDiscountAmount.multiply(itemValueRatio);
//
//        // Tính giá sau giảm giá cho một sản phẩm
//        BigDecimal discountedItemPrice = originalItemPrice.subtract(
//                itemDiscountAmount.divide(BigDecimal.valueOf(billDetail.getQuantity()), 2, RoundingMode.HALF_UP));
//
//        // Tính tổng số tiền hoàn trả
//        return discountedItemPrice.multiply(BigDecimal.valueOf(returnQuantity));
//    }
//
//    public List<ReturnOrder> getReturnOrdersByBillId(Long billId) {
//        return returnOrderRepository.findByBillId(billId);
//    }
//
//    @Transactional
//    public ReturnOrder updateReturnOrderStatus(Long returnOrderId, int newStatus) {
//        ReturnOrder returnOrder = returnOrderRepository.findById(returnOrderId)
//                .orElseThrow(() -> new RuntimeException("Return order not found"));
//
//        returnOrder.setReturnStatus(newStatus);
//        returnOrder.setUpdatedAt(new Date());
//
//        return returnOrderRepository.save(returnOrder);
//    }
//
//    public BigDecimal calculateTotalRefundAmount(Long billId) {
//        List<ReturnOrder> returnOrders = getReturnOrdersByBillId(billId);
//        Bill bill = billRepository.findById(billId)
//                .orElseThrow(() -> new RuntimeException("Bill not found"));
//
//        BigDecimal totalDiscount = bill.getTotalAmountAfterDiscount().subtract(bill.getTotalAmount());
//        BigDecimal discountRatio = totalDiscount.divide(bill.getTotalAmount(), 4, RoundingMode.HALF_UP);
//
//        return returnOrders.stream()
//                .map(ro -> calculateRefundAmount(ro.getBill(), ro.getBillDetail(), ro))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
}
