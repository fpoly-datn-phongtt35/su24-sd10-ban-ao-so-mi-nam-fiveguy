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

//        updateBillDetailsQuantities(savedReturnOrders);

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
//        BigDecimal newTotalAmount = BigDecimal.ZERO;
//        for (BillDetail detail : billDetailRepository.findAllByBillIdOrderByIdDesc(bill.getId())) {
//            newTotalAmount = newTotalAmount.add(
//                    detail.getPromotionalPrice().multiply(BigDecimal.valueOf(detail.getQuantity()))
//            );
//        }
//        bill.setTotalAmount(newTotalAmount);
//        bill.setTotalAmountAfterDiscount(summary.getTongTienDaGiamSauKhiTra());
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


    //Hiển thị
    public ReturnOrderSummary calculateReturnOrderSummary(Long billId, List<ReturnOrder> returnOrders) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        if (returnOrders.isEmpty()) return null;

        ReturnOrderSummary summary = new ReturnOrderSummary();

        BigDecimal tiLeGiam = BigDecimal.ONE.subtract(
                bill.getTotalAmountAfterDiscount().divide(bill.getTotalAmount(), 50, RoundingMode.HALF_UP)
        );

        BigDecimal tongTienTra = BigDecimal.ZERO;
        int tongSoLuongTraLai = 0;

        for (ReturnOrder returnOrder : returnOrders) {
            BigDecimal giaGoc = returnOrder.getBillDetail().getPromotionalPrice();
            int soLuong = returnOrder.getQuantity();

//            BigDecimal tienHoanTra = giaGoc.multiply(BigDecimal.valueOf(soLuong))
//                    .multiply(BigDecimal.ONE.subtract(tiLeGiam)).setScale(0, RoundingMode.HALF_UP);
            BigDecimal tienHoanTra = giaGoc.multiply(BigDecimal.valueOf(soLuong))
                    .multiply(BigDecimal.ONE.subtract(tiLeGiam));
            tongTienTra = tongTienTra.add(tienHoanTra);
            tongSoLuongTraLai += soLuong;
        }

        summary.setTongTien(bill.getTotalAmount().setScale(0, RoundingMode.HALF_UP));
        summary.setTongTienDaGiam(bill.getTotalAmountAfterDiscount().setScale(0, RoundingMode.HALF_UP));
        summary.setTongTienSauKhiTra(bill.getTotalAmount().subtract(tongTienTra).setScale(0, RoundingMode.HALF_UP));
        summary.setTongTienDaGiamSauKhiTra(bill.getTotalAmountAfterDiscount().subtract(tongTienTra).setScale(0, RoundingMode.HALF_UP));
        summary.setTiLeGiam(tiLeGiam.setScale(50, RoundingMode.HALF_UP));
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


    //Tính tiền hoàn
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


    //Hiển thị
    public ReturnOrderSummary calculateBillDetailSummary(Long billId, List<BillDetail> billDetails) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        if (billDetails.isEmpty()) return null;

        ReturnOrderSummary summary = new ReturnOrderSummary();

        BigDecimal tiLeGiam = BigDecimal.ONE.subtract(
                bill.getTotalAmountAfterDiscount().divide(bill.getTotalAmount(), 50, RoundingMode.HALF_UP)
        );

        BigDecimal tongTienTra = BigDecimal.ZERO;
        int tongSoLuongTraLai = 0;

        for (BillDetail billDetail : billDetails) {
            BigDecimal giaGoc = billDetail.getPromotionalPrice();
            int soLuong = billDetail.getQuantity();

//            BigDecimal tienHoanTra = giaGoc.multiply(BigDecimal.valueOf(soLuong))
//                    .multiply(BigDecimal.ONE.subtract(tiLeGiam)).setScale(0, RoundingMode.HALF_UP);
            BigDecimal tienHoanTra = giaGoc.multiply(BigDecimal.valueOf(soLuong))
                    .multiply(BigDecimal.ONE.subtract(tiLeGiam));

            tongTienTra = tongTienTra.add(tienHoanTra);
            tongSoLuongTraLai += soLuong;
        }

        summary.setTongTien(bill.getTotalAmount().setScale(0, RoundingMode.HALF_UP));
        summary.setTongTienDaGiam(bill.getTotalAmountAfterDiscount().setScale(0, RoundingMode.HALF_UP));
        summary.setTongTienSauKhiTra(bill.getTotalAmount().subtract(tongTienTra).setScale(0, RoundingMode.HALF_UP));
        summary.setTongTienDaGiamSauKhiTra(bill.getTotalAmountAfterDiscount().subtract(tongTienTra).setScale(0, RoundingMode.HALF_UP));
        summary.setTiLeGiam(tiLeGiam.setScale(50, RoundingMode.HALF_UP));
        summary.setTongSoLuong(bill.getBillDetail().stream().mapToInt(BillDetail::getQuantity).sum());
        summary.setTongSoLuongTraLai(tongSoLuongTraLai);
        summary.setGiaTrungBinhSanPham(bill.getTotalAmount()
                .divide(BigDecimal.valueOf(summary.getTongSoLuong()), 0, RoundingMode.HALF_UP));
        summary.setGiaTrungBinhSanPhamDaGiam(bill.getTotalAmountAfterDiscount()
                .divide(BigDecimal.valueOf(summary.getTongSoLuong()), 0, RoundingMode.HALF_UP));
        summary.setTongTienTra(tongTienTra.setScale(0, RoundingMode.HALF_UP));

        return summary;
    }


    //Cách tính theo tỉ lệ
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

}
