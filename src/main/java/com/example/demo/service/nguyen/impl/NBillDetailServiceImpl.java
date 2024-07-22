package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.entity.ProductDetail;
import com.example.demo.model.response.nguyen.BillDetailSummary;
import com.example.demo.repository.nguyen.bill.NBillDetailRepository;
import com.example.demo.repository.nguyen.bill.NBillRepository;
import com.example.demo.repository.nguyen.product.NProductDetailRepository;
import com.example.demo.service.nguyen.NBillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class NBillDetailServiceImpl implements NBillDetailService {

    @Autowired
    NBillDetailRepository billDetailRepository;

    @Autowired
    NBillRepository billRepository;

    @Autowired
    NProductDetailRepository productDetailRepository;


    @Override
    public BillDetail getById(Long id) {
        return billDetailRepository.findById(id).get();
    }

    @Override
    public List<BillDetail> getAllByBillId(Long billId) {
        return billDetailRepository.findAllByBillIdOrderByIdDesc(billId);
    }

    @Override
    public BillDetailSummary getBillDetailSummaryByBillId(Long billId) {
        List<BillDetail> billDetails = billDetailRepository.findByBillId(billId);

        int totalQuantity = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalPromotionalPrice = BigDecimal.ZERO;

        for (BillDetail billDetail : billDetails) {
            totalQuantity += billDetail.getQuantity();
            totalPrice = totalPrice.add(billDetail.getPrice()
                    .multiply(BigDecimal.valueOf(billDetail.getQuantity())));
            totalPromotionalPrice = totalPromotionalPrice.add(billDetail.getPromotionalPrice()
                    .multiply(BigDecimal.valueOf(billDetail.getQuantity())));
        }

        BillDetailSummary summary = new BillDetailSummary();
        summary.setTotalQuantity(totalQuantity);
        summary.setTotalPrice(totalPrice);
        summary.setTotalPromotionalPrice(totalPromotionalPrice);

        return summary;
    }

    @Transactional
    public BillDetail addProductDetailToBill(Long billId, Long productDetailId, int quantity,
                                             BigDecimal price, BigDecimal promotionalPrice) {
        // Fetch the bill
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billId));

        // Fetch the product detail
        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new RuntimeException(
                        "Product detail not found with id: " + productDetailId));

        // Check if the product detail already exists in the bill
        Optional<BillDetail> existingBillDetailOptional = billDetailRepository
                .findByBillAndProductDetail(bill, productDetail);
        if (existingBillDetailOptional.isPresent()) {
            // Update existing BillDetail quantity and price
            BillDetail existingBillDetail = existingBillDetailOptional.get();
            existingBillDetail.setQuantity(existingBillDetail.getQuantity() + quantity);
            existingBillDetail.setPrice(price);
            existingBillDetail.setPromotionalPrice(promotionalPrice);

            // Save the updated BillDetail
            existingBillDetail = billDetailRepository.save(existingBillDetail);

            // Update the Bill's total amount
            BigDecimal totalPriceIncrement = price.multiply(BigDecimal.valueOf(quantity));
            bill.setTotalAmount(bill.getTotalAmount().add(totalPriceIncrement));
            billRepository.save(bill);

            // Update the ProductDetail quantity
//            productDetail.setQuantity(productDetail.getQuantity() - quantity);
//            productDetailRepository.save(productDetail);

            return existingBillDetail;
        } else {
            // Create a new BillDetail instance
            BillDetail billDetail = new BillDetail();
            billDetail.setBill(bill);
            billDetail.setProductDetail(productDetail);
            billDetail.setQuantity(quantity);
            billDetail.setPrice(price);
            billDetail.setPromotionalPrice(promotionalPrice);
            billDetail.setDefectiveProduct(0);
            billDetail.setStatus(1);

            // Save the BillDetail instance
            billDetail = billDetailRepository.save(billDetail);

            // Update the Bill's total amount
            BigDecimal totalPriceIncrement = price.multiply(BigDecimal.valueOf(quantity));
            bill.setTotalAmount(bill.getTotalAmount().add(totalPriceIncrement));
            billRepository.save(bill);

            // Update the ProductDetail quantity
//            productDetail.setQuantity(productDetail.getQuantity() - quantity);
//            productDetailRepository.save(productDetail);

            return billDetail;
        }
    }

    @Transactional
    public void removeProductDetailFromBill(Long billDetailId) {
        // Fetch the BillDetail
        BillDetail billDetail = billDetailRepository.findById(billDetailId)
                .orElseThrow(() -> new RuntimeException(
                        "BillDetail not found with id: " + billDetailId));

        // Fetch the associated Bill
        Bill bill = billDetail.getBill();

        // Subtract the BillDetail amount from the Bill's total amount
        BigDecimal amountToSubtract = billDetail.getPrice()
                .multiply(BigDecimal.valueOf(billDetail.getQuantity()));
        bill.setTotalAmount(bill.getTotalAmount().subtract(amountToSubtract));

        // Update the ProductDetail quantity
        ProductDetail productDetail = billDetail.getProductDetail();
//        productDetail.setQuantity(productDetail.getQuantity() + billDetail.getQuantity());
//        productDetailRepository.save(productDetail);

        // Save the updated Bill
        billRepository.save(bill);

        // Delete the BillDetail
        billDetailRepository.delete(billDetail);
    }

    @Transactional
    public BillDetail updateBillDetailQuantity(Long billDetailId, int newQuantity) {
        // Fetch the BillDetail
        BillDetail billDetail = billDetailRepository.findById(billDetailId)
                .orElseThrow(() -> new RuntimeException(
                        "BillDetail not found with id: " + billDetailId));

        // Fetch the associated Bill and ProductDetail
        Bill bill = billDetail.getBill();
        ProductDetail productDetail = billDetail.getProductDetail();

        // Calculate the quantity difference
        int quantityDifference = newQuantity - billDetail.getQuantity();

        // Update the BillDetail quantity
        billDetail.setQuantity(newQuantity);

        // Update the ProductDetail quantity
//        productDetail.setQuantity(productDetail.getQuantity() - quantityDifference);
//        productDetailRepository.save(productDetail);

        // Update the Bill's total amount
        BigDecimal amountDifference = billDetail.getPrice()
                .multiply(BigDecimal.valueOf(quantityDifference));
        bill.setTotalAmount(bill.getTotalAmount().add(amountDifference));
        billRepository.save(bill);

        // Save the updated BillDetail
        return billDetailRepository.save(billDetail);
    }
}
