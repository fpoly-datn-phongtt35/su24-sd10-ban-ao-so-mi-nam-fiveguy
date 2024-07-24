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

        ReturnOrderSummary returnOrderSummary = calculateRefundSummary(bill, returnOrders);

        List<ReturnOrder> savedReturnOrders = returnOrderRepository.saveAll(returnOrders);

        updateBillDetailsQuantities(savedReturnOrders);

        PaymentStatus newPaymentStatus = createNewPaymentStatus(bill, returnOrderSummary.getTongTienTra());
        paymentStatusRepository.save(newPaymentStatus);

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
        bill.setStatus(23);  // Đảm bảo 23 là trạng thái đúng
        billRepository.save(bill);
    }

    private BillHistory createNewBillHistory(Bill bill, String fullName) {
        BillHistory newBillHistory = new BillHistory();
        newBillHistory.setBill(bill);
        newBillHistory.setCreatedAt(new Date());
        newBillHistory.setReason(0);
        newBillHistory.setType(1);
        newBillHistory.setCreatedBy(fullName);
        newBillHistory.setStatus(23);  // Đảm bảo 23 là trạng thái đúng
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
        newBill.setStatus(23);

        billRepository.save(newBill);

        BillHistory newBillHistory = new BillHistory();
        newBillHistory.setBill(bill.get());
        newBillHistory.setCreatedAt(new Date());
        newBillHistory.setReason(0);
        newBillHistory.setType(1);
        newBillHistory.setCreatedBy(fullName);
        newBillHistory.setStatus(23);
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


//
//    public static void main(String[] args) {
//        // Khởi tạo dữ liệu
//        Bill bill = new Bill();
//        bill.setTotalAmount(new BigDecimal("15000000"));
//        bill.setTotalAmountAfterDiscount(new BigDecimal("12000000"));
//
//        BillDetail billDetail1 = new BillDetail();
//        billDetail1.setQuantity(10);
//
//        BillDetail billDetail2 = new BillDetail();
//        billDetail2.setQuantity(7);
//
//        bill.setBillDetail(List.of(billDetail1, billDetail2));
//
//        ReturnOrder returnOrder = new ReturnOrder();
//        returnOrder.setQuantity(9);
//        returnOrder.setBill(bill);
//
//        List<ReturnOrder> returnOrders = List.of(returnOrder);
//
//        // Tính các thông tin và số tiền phải trả lại
//        ReturnOrderSummary summary = calculateRefundSummary(bill, returnOrders);
//
//        // In các thông tin
//        System.out.println("Tổng giá trị đơn hàng: " + summary.getTongTien());
//        System.out.println("Tổng giá trị đơn hàng sau khi giảm: " + summary.getTongTienDaGiam());
//        System.out.println("Tỷ lệ giảm giá: " + summary.getTiLeGiam());
//        System.out.println("Tổng số lượng sản phẩm: " + summary.getTongSoLuong());
//        System.out.println("Tổng số lượng sản phẩm hoàn trả: " + summary.getTongSoLuongTraLai());
//        System.out.println("Giá trị trung bình mỗi sản phẩm: " + summary.getGiaTrungBinhSanPham());
//        System.out.println("Giá trị trung bình mỗi sản phẩm sau khi giảm: " +
//                summary.getGiaTrungBinhSanPhamDaGiam());
//        System.out.println("Tổng số tiền phải trả lại: " + summary.getTongTienTra());
//        System.out.println("Tổng giá trị đơn hàng sau khi trả: " + summary.getTongTienSauKhiTra());
//        System.out.println("Tổng giá trị đơn hàng đã giảm sau khi trả: " +
//                summary.getTongTienDaGiamSauKhiTra());
//    }


//    public ReturnOrderSumary calculateReturnOrderSumary(Bill bill,
//                                                        List<ReturnOrder> returnOrders) {
//        ReturnOrderSumary summary = new ReturnOrderSumary();
//
//        // Tổng giá trị đơn hàng trước khi giảm giá
//        BigDecimal totalAmount = bill.getTotalAmount();
//        summary.setTotalAmount(totalAmount);
//
//        // Tổng giá trị đơn hàng sau khi giảm giá
//        BigDecimal totalAmountAfterDiscount = bill.getTotalAmountAfterDiscount();
//        summary.setTotalAmountAfterDiscount(totalAmountAfterDiscount);
//
//        // Tổng số lượng sản phẩm trong đơn hàng
//        int totalQuantity = bill.getBillDetail().stream().mapToInt(BillDetail::getQuantity).sum();
//        summary.setTotalQuantity(totalQuantity);
//
//        // Giá trị trung bình mỗi sản phẩm sau khi giảm giá
//        BigDecimal averageProductPriceAfterDiscount = totalAmountAfterDiscount
//                .divide(BigDecimal.valueOf(totalQuantity), RoundingMode.HALF_UP);
//        summary.setAverageProductPriceAfterDiscount(averageProductPriceAfterDiscount);
//
//        // Tỷ lệ giảm giá
//        BigDecimal discountRate = BigDecimal.ONE.subtract(
//                totalAmountAfterDiscount.divide(totalAmount, 10, RoundingMode.HALF_UP)
//        );
//        summary.setDiscountRate(discountRate);
//
//        // Tổng số lượng sản phẩm trả hàng
//        int totalReturnQuantity = returnOrders.stream().mapToInt(ReturnOrder::getQuantity).sum();
//        summary.setTotalReturnQuantity(totalReturnQuantity);
//
//        // Tổng số tiền phải trả lại
//        BigDecimal totalRefundAmount = averageProductPriceAfterDiscount
//                .multiply(BigDecimal.valueOf(totalReturnQuantity));
//        summary.setTotalRefundAmount(totalRefundAmount);
//
//        return summary;
//    }
//
//    public BigDecimal calculateRefundAmount(Bill bill, List<ReturnOrder> returnOrders) {
//        BigDecimal totalRefund = BigDecimal.ZERO;
//        BigDecimal totalDiscount = bill.getTotalAmount()
//                .subtract(bill.getTotalAmountAfterDiscount());
//
//        List<BillDetail> billDetails = billDetailRepository
//                .findAllByBillIdOrderByIdDesc(bill.getId());
//
//        Integer totalQuantityBillDetail = 0;
//        for (BillDetail billDetail : billDetails) {
//            totalQuantityBillDetail = totalQuantityBillDetail + billDetail.getQuantity();
//        }
//        System.out.println("TỔng số sản phẩm trong bill" + totalQuantityBillDetail);
//
//        for (ReturnOrder returnOrder : returnOrders) {
//            BillDetail billDetail = returnOrder.getBillDetail();
//            BigDecimal itemPrice = billDetail.getPromotionalPrice();
//            int returnQuantity = returnOrder.getQuantity();
//
//            // Calculate proportionate discount for the returned items
//            BigDecimal itemTotalPrice = itemPrice
//                    .multiply(BigDecimal.valueOf(billDetail.getQuantity()));
//            BigDecimal itemDiscount = totalDiscount.multiply(itemTotalPrice)
//                    .divide(bill.getTotalAmount(), RoundingMode.HALF_UP);
//            BigDecimal itemRefund = itemPrice.multiply(BigDecimal.valueOf(returnQuantity))
//                    .subtract(itemDiscount);
//
//            totalRefund = totalRefund.add(itemRefund);
//        }
//
//        return totalRefund;
//    }
//
//    public BigDecimal calculateRefund(Bill bill, List<ReturnOrder> returnOrders) {
//        // Kiểm tra dữ liệu đầu vào
//        if (bill.getTotalAmount() == null || bill.getTotalAmountAfterDiscount() == null ||
//                bill.getTotalAmount().compareTo(BigDecimal.ZERO) == 0) {
//            throw new IllegalArgumentException(
//                    "Total amount or total amount after discount cannot be null or zero.");
//        }
//
//        // Tổng giá trị đơn hàng trước khi giảm giá
//        BigDecimal totalAmount = bill.getTotalAmount();
//
//        // Tổng giá trị đơn hàng sau khi giảm giá
//        BigDecimal totalAmountAfterDiscount = bill.getTotalAmountAfterDiscount();
//
//        // Tỷ lệ giảm giá
//        BigDecimal discountRate = BigDecimal.ONE.subtract(
//                totalAmountAfterDiscount.divide(totalAmount, 10, RoundingMode.HALF_UP)
//        );
//
//        // Tổng số lượng sản phẩm trong đơn hàng
//        int totalQuantity = bill.getBillDetail().stream().mapToInt(BillDetail::getQuantity).sum();
//
//        // Giá trị trung bình mỗi sản phẩm trước khi giảm giá
//        BigDecimal averageProductPrice = totalAmount
//                .divide(BigDecimal.valueOf(totalQuantity), 10, RoundingMode.HALF_UP);
//
//        // Tổng số tiền phải trả lại cho các sản phẩm yêu cầu hoàn
//        BigDecimal totalRefundAmount = BigDecimal.ZERO;
//
//        for (ReturnOrder returnOrder : returnOrders) {
//            // Số lượng sản phẩm yêu cầu hoàn
//            int returnQuantity = returnOrder.getQuantity();
//
//            // Giá trị của sản phẩm yêu cầu hoàn trước khi giảm giá
//            BigDecimal returnProductValue = averageProductPrice
//                    .multiply(BigDecimal.valueOf(returnQuantity));
//
//            // Số tiền giảm giá cho sản phẩm yêu cầu hoàn
//            BigDecimal discountAmount = returnProductValue.multiply(discountRate);
//
//            // Số tiền phải trả lại
//            BigDecimal refundAmount = returnProductValue.subtract(discountAmount);
//
//            // Cộng dồn vào tổng số tiền phải trả lại
//            totalRefundAmount = totalRefundAmount.add(refundAmount);
//        }
//
//        return totalRefundAmount.setScale(0, RoundingMode.HALF_UP); // Làm tròn kết quả
//    }
//
//    public static ReturnOrderSumary calculateReturnOrder(Bill bill,
//                                                         List<ReturnOrder> returnOrders) {
//        ReturnOrderSumary summary = new ReturnOrderSumary();
//
//        // Tổng giá trị đơn hàng trước khi giảm giá
//        BigDecimal totalAmount = bill.getTotalAmount();
//        summary.setTotalAmount(totalAmount);
//
//        // Tổng giá trị đơn hàng sau khi giảm giá
//        BigDecimal totalAmountAfterDiscount = bill.getTotalAmountAfterDiscount();
//        summary.setTotalAmountAfterDiscount(totalAmountAfterDiscount);
//
//        // Tỷ lệ giảm giá
//        BigDecimal discountRate = BigDecimal.ONE.subtract(
//                totalAmountAfterDiscount.divide(totalAmount, 10, RoundingMode.HALF_UP)
//        );
//        summary.setDiscountRate(discountRate);
//
//        // Tổng số lượng sản phẩm trong đơn hàng
//        int totalQuantity = bill.getBillDetail().stream().mapToInt(BillDetail::getQuantity).sum();
//        summary.setTotalQuantity(totalQuantity);
//
//        // Giá trị trung bình mỗi sản phẩm trước khi giảm giá
//        BigDecimal averageProductPriceBeforeDiscount = totalAmount
//                .divide(BigDecimal.valueOf(totalQuantity), 10, RoundingMode.HALF_UP);
//        summary.setAverageProductPriceBeforeDiscount(averageProductPriceBeforeDiscount);
//
//        // Giá trị trung bình mỗi sản phẩm sau khi giảm giá
//        BigDecimal averageProductPriceAfterDiscount = totalAmountAfterDiscount
//                .divide(BigDecimal.valueOf(totalQuantity), 10, RoundingMode.HALF_UP);
//        summary.setAverageProductPriceAfterDiscount(averageProductPriceAfterDiscount);
//
//        // Tổng số lượng sản phẩm trả hàng
//        int totalReturnQuantity = returnOrders.stream().mapToInt(ReturnOrder::getQuantity).sum();
//        summary.setTotalReturnQuantity(totalReturnQuantity);
//
//        // Giá trị của các sản phẩm trước khi hoàn
//        BigDecimal totalAmountBeforeRefund = averageProductPriceBeforeDiscount
//                .multiply(BigDecimal.valueOf(totalReturnQuantity));
//        summary.setTotalAmountBeforeRefund(totalAmountBeforeRefund);
//
//        // Giá trị của các sản phẩm sau khi hoàn
//        BigDecimal setTotalAmountDiscountAfterRefund = totalAmountAfterDiscount.subtract(
//                averageProductPriceAfterDiscount.multiply(BigDecimal.valueOf(totalReturnQuantity))
//        );
//        summary.setTotalAmountDiscountAfterRefund(setTotalAmountDiscountAfterRefund);
//
//        // Tổng số tiền phải trả lại cho các sản phẩm yêu cầu hoàn
//        BigDecimal totalRefundAmount = BigDecimal.ZERO;
//        for (ReturnOrder returnOrder : returnOrders) {
//            // Số lượng sản phẩm yêu cầu hoàn
//            int returnQuantity = returnOrder.getQuantity();
//
//            // Giá trị của sản phẩm yêu cầu hoàn trước khi giảm giá
//            BigDecimal returnProductValue = averageProductPriceBeforeDiscount
//                    .multiply(BigDecimal.valueOf(returnQuantity));
//
//            // Số tiền giảm giá cho sản phẩm yêu cầu hoàn
//            BigDecimal discountAmount = returnProductValue.multiply(discountRate);
//
//            // Số tiền phải trả lại
//            BigDecimal refundAmount = returnProductValue.subtract(discountAmount);
//
//            // Cộng dồn vào tổng số tiền phải trả lại
//            totalRefundAmount = totalRefundAmount.add(refundAmount);
//        }
//
//        summary.setTotalRefundAmount(totalRefundAmount.setScale(0, RoundingMode.HALF_UP));
//
//        return summary;
//    }
}
