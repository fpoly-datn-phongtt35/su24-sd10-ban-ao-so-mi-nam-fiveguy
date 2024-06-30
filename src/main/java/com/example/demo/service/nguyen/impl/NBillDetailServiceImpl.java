package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.entity.ProductDetail;
import com.example.demo.repository.nguyen.NBillDetailRepository;
import com.example.demo.repository.nguyen.NBillRepository;
import com.example.demo.repository.nguyen.product.NProductDetailRepository;
import com.example.demo.service.nguyen.NBillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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

        // Create a new BillDetail instance
        BillDetail billDetail = new BillDetail();
        billDetail.setBill(bill);
        billDetail.setProductDetail(productDetail);
        billDetail.setQuantity(quantity);
        billDetail.setPrice(price);
        billDetail.setPromotionalPrice(promotionalPrice);
        billDetail.setStatus(1);

        // Save the BillDetail instance
        billDetail = billDetailRepository.save(billDetail);

        // Update the Bill's total amount
        bill.setTotalAmount(
                bill.getTotalAmount().add(price.multiply(BigDecimal.valueOf(quantity))));
        billRepository.save(bill);

        // Update the ProductDetail quantity
        productDetail.setQuantity(productDetail.getQuantity() - quantity);
        productDetailRepository.save(productDetail);

        return billDetail;
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
        productDetail.setQuantity(productDetail.getQuantity() + billDetail.getQuantity());
        productDetailRepository.save(productDetail);

        // Save the updated Bill
        billRepository.save(bill);

        // Delete the BillDetail
        billDetailRepository.delete(billDetail);
    }

    @Transactional
    public BillDetail updateBillDetailQuantity(Long billDetailId, int newQuantity) {
        // Fetch the BillDetail
        BillDetail billDetail = billDetailRepository.findById(billDetailId)
                .orElseThrow(() -> new RuntimeException("BillDetail not found with id: " + billDetailId));

        // Fetch the associated Bill and ProductDetail
        Bill bill = billDetail.getBill();
        ProductDetail productDetail = billDetail.getProductDetail();

        // Calculate the quantity difference
        int quantityDifference = newQuantity - billDetail.getQuantity();

        // Update the BillDetail quantity
        billDetail.setQuantity(newQuantity);

        // Update the ProductDetail quantity
        productDetail.setQuantity(productDetail.getQuantity() - quantityDifference);
        productDetailRepository.save(productDetail);

        // Update the Bill's total amount
        BigDecimal amountDifference = billDetail.getPrice().multiply(BigDecimal.valueOf(quantityDifference));
        bill.setTotalAmount(bill.getTotalAmount().add(amountDifference));
        billRepository.save(bill);

        // Save the updated BillDetail
        return billDetailRepository.save(billDetail);
    }
}
