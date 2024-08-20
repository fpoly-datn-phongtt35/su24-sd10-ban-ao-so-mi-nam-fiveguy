package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.advice.DuplicateException;
import com.example.demo.entity.*;
import com.example.demo.model.response.thuong.BillResponseTH;
import com.example.demo.repository.thuong.*;
import com.example.demo.service.thuong.BillServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillServiceTHImpl implements BillServiceTH {

    @Autowired
    private BillRepositoryTH billRepository;

    @Autowired
    private ProductDetailRepositoryTH productDetailRepository;

    @Autowired
    private BillDetailsRepositoryTH billDetailsRepository;

    @Autowired
    private PaymentMethodRepositoryTH paymentMethodRepository;

    @Autowired
    private ProductRepositoryTH productRepository;

    @Autowired
    private BillHistoryRepositoryTH billHistoryRepository;

    @Autowired
    private PaymentStatusRepositoryTH paymentStatusRepository;

    @Override
    public List<BillResponseTH> findAllByStatusAndTypeBill(Integer status, Integer typeBill) {
        return billRepository.findAllByStatusAndTypeBill(status, typeBill).stream().map(b -> {
            b.setBillDetail(billDetailsRepository.findAllByBill_Id(b.getId()));
            return b;
        }).collect(Collectors.toList());
    }

    public BillResponseTH setBillResponse(Bill bill) {
        BillResponseTH billResponse = new BillResponseTH();
        billResponse.setId(bill.getId());
        billResponse.setCode(bill.getCode());
        billResponse.setReciverName(bill.getReciverName());
        billResponse.setDeliveryDate(bill.getDeliveryDate());
        billResponse.setShippingFee(bill.getShippingFee());
        billResponse.setAddressId(bill.getAddressId());
        billResponse.setAddress(bill.getAddress());
        billResponse.setPhoneNumber(bill.getPhoneNumber());
        billResponse.setTotalAmount(bill.getTotalAmount());
        billResponse.setTotalAmountAfterDiscount(bill.getTotalAmountAfterDiscount());
        billResponse.setPaidAmount(bill.getPaidAmount());
        billResponse.setPaidShippingFee(bill.getPaidShippingFee());
        billResponse.setCreatedAt(bill.getCreatedAt());
        billResponse.setCustomer(bill.getCustomer());
        billResponse.setEmployee(bill.getEmployee());
        billResponse.setPaymentMethod(bill.getPaymentMethod());
        billResponse.setVoucher(bill.getVoucher());
        billResponse.setTypeBill(bill.getTypeBill());
        billResponse.setNote(bill.getNote());
        billResponse.setStatus(bill.getStatus());
        billResponse.setBillDetail(billDetailsRepository.findAllByBill_Id(bill.getId()));
        billResponse.setPaymentStatus(paymentStatusRepository.findByPaymentMethodAndBill_Id(2, bill.getId()));
        return billResponse;
    }

    @Override
    public BillResponseTH getOne(Long id) {
        Bill bill = billRepository.findById(id).orElse(null);
        if (bill == null) return null;
        BillResponseTH billResponse = setBillResponse(bill);
        return billResponse;
    }

    private void total(Bill bill) {
        bill.setTotalAmount(bill.getBillDetail().stream()
                .map(detail -> detail.getPromotionalPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private ProductDetail checkProductDetail(Long id) {
        ProductDetail pd = productDetailRepository.findById(id).orElse(null);

        if (pd == null) return null;

        if (pd.getQuantity() < 1 ) {
            throw new DuplicateException("Không đủ số lượng tồn kho cho " + pd.getProduct().getName() + " " + pd.getColor().getName(), "alert");

        } else if (pd.getStatus() == 0 || pd.getProduct().getStatus() == 0) {

            throw new DuplicateException(pd.getProduct().getName() + " " + pd.getColor().getName() + " đã ngừng kinh doanh", "alert");
        }
        pd.setQuantity(pd.getQuantity() - 1);
        return productDetailRepository.save(pd);
    }

    private ProductDetail checkProductDetailRemove(Long id) {
        ProductDetail pd = productDetailRepository.findById(id).orElse(null);

        if (pd == null) return null;
        pd.setQuantity(pd.getQuantity() + 1);
        return productDetailRepository.save(pd);
    }

    private ProductDetail checkProductDetailUpdate(Long id, BillDetail bd, Integer updateQty) {
        ProductDetail pd = productDetailRepository.findById(id).orElse(null);

        if (pd == null) return null;
        if (bd.getQuantity() < updateQty) {
            if (pd.getStatus() == 0  || pd.getProduct().getStatus() == 0) {
                throw new DuplicateException(pd.getProduct().getName() + " " + pd.getColor().getName() + " đã ngừng kinh doanh", "alert");
            }
            else if (pd.getQuantity() < (updateQty - bd.getQuantity())) {
                throw new DuplicateException("Không đủ số lượng tồn kho cho " + pd.getProduct().getName() + " " + pd.getColor().getName(), "alert");
            } else if (pd.getQuantity() >= (updateQty - bd.getQuantity())) {
                pd.setQuantity(pd.getQuantity() - (updateQty - bd.getQuantity()));
            }
        }
        if (bd.getQuantity() > updateQty) {
            pd.setQuantity(pd.getQuantity() + (bd.getQuantity() - updateQty));
        }

        return productDetailRepository.save(pd);
    }

    private void checkProductDetailDelete(Long id, BillDetail bd) {
        ProductDetail pd = productDetailRepository.findById(id).orElse(null);
        if (pd == null) return;
        pd.setQuantity(pd.getQuantity() + bd.getQuantity());
        productDetailRepository.save(pd);
    }


    @Override
    public BillResponseTH addProductCart(BillResponseTH billRequest, Long id) {
        Optional<Bill> billOptional = billRepository.findById(billRequest.getId());
        if (billOptional.isEmpty()) {
           return null;
        }
        Bill bill = billOptional.get();

        BillDetail bd = billDetailsRepository.findByBill_Id(bill.getId());

        if (bill.getBillDetail() != null && bd != null) {
            bd.setBill(bill);
            bd.setProductDetail(checkProductDetail(id));
            bd.setQuantity(bd.getQuantity() + 1);
            bd.setPrice(bd.getProductDetail().getProduct().getPrice());
            Integer promotinalPrice = productRepository.findPromotionalPriceByProductId(bd.getProductDetail().getProduct().getId());
            bd.setPromotionalPrice(promotinalPrice != null ? BigDecimal.valueOf(promotinalPrice) : bd.getProductDetail().getProduct().getPrice());
            bill.getBillDetail().add(bd);
        } else {
            BillDetail bd2 = new BillDetail();
            bd2.setBill(bill);
            bd2.setProductDetail(checkProductDetail(id));
            bd2.setQuantity(1);
            bd2.setPrice(bd2.getProductDetail().getProduct().getPrice());
            Integer promotinalPrice2 = productRepository.findPromotionalPriceByProductId(bd2.getProductDetail().getProduct().getId());
            bd2.setPromotionalPrice(promotinalPrice2 != null ? BigDecimal.valueOf(promotinalPrice2) : bd2.getProductDetail().getProduct().getPrice());
            bill.getBillDetail().add(bd2);
        }
        total(bill);
        return setBillResponse(billRepository.save(bill));
    }

    @Override
    public BillResponseTH removeProductCart(BillResponseTH billRequest, Long id) {
        Optional<Bill> billOptional = billRepository.findById(billRequest.getId());
        if (billOptional.isEmpty()) {
            return null;
        }
        Bill bill = billOptional.get();

        BillDetail bd = billDetailsRepository.findByBill_Id(bill.getId());

        if (bill.getBillDetail() != null && bd != null) {
            bd.setProductDetail(checkProductDetailRemove(id));
            bd.setQuantity(bd.getQuantity() - 1);
            if (bd.getQuantity() == 0) {
                billDetailsRepository.delete(bd);
                total(bill);
                return setBillResponse(billRepository.save(bill));
            }
            bd.setBill(bill);
            bd.setPrice(bd.getProductDetail().getProduct().getPrice());
            Integer promotinalPrice = productRepository.findPromotionalPriceByProductId(bd.getProductDetail().getProduct().getId());
            bd.setPromotionalPrice(promotinalPrice != null ? BigDecimal.valueOf(promotinalPrice) : bd.getProductDetail().getProduct().getPrice());

        }
        total(bill);
        return setBillResponse(billRepository.save(bill));
    }

    @Override
    public BillResponseTH updateProductCart(BillResponseTH billRequest, Long id, Integer updateQty) {
        Optional<Bill> billOptional = billRepository.findById(billRequest.getId());
        if (billOptional.isEmpty()) {
            return null;
        }
        Bill bill = billOptional.get();

        BillDetail bd = billDetailsRepository.findByBill_Id(bill.getId());
        if (bd.getQuantity() == updateQty) {
            return billRequest;
        }
        if (bill.getBillDetail() != null && bd != null) {
            bd.setProductDetail(checkProductDetailUpdate(id, bd, updateQty));
            bd.setQuantity(updateQty);
            bd.setBill(bill);
            bd.setPrice(bd.getProductDetail().getProduct().getPrice());
            Integer promotinalPrice = productRepository.findPromotionalPriceByProductId(bd.getProductDetail().getProduct().getId());
            bd.setPromotionalPrice(promotinalPrice != null ? BigDecimal.valueOf(promotinalPrice) : bd.getProductDetail().getProduct().getPrice());

        }
        total(bill);
        return setBillResponse(billRepository.save(bill));
    }

    @Override
    public BillResponseTH deleteProductCart(BillResponseTH billRequest, Long id) {
        Optional<Bill> billOptional = billRepository.findById(billRequest.getId());
        if (billOptional.isEmpty()) {
            return null;
        }
        Bill bill = billOptional.get();

        BillDetail bd = billDetailsRepository.findByBill_Id(bill.getId());

        checkProductDetailDelete(id, bd);
        billDetailsRepository.delete(bd);
        total(bill);

        return setBillResponse(billRepository.save(bill));
    }

    @Override
    public Bill deleteBill(Long id) {
        Optional<Bill> billOptional = billRepository.findById(id);
        if (billOptional.isEmpty()) {
            return null;
        }
        Bill bill = billOptional.get();
        List<BillDetail> listBD = billDetailsRepository.findAllByBill_Id(id);
        if (listBD.size() > 0) {
            for (BillDetail billDetail : listBD) {
                ProductDetail productDetail = billDetail.getProductDetail();
                if (productDetail != null) {
                    productDetail.setQuantity(productDetail.getQuantity() + billDetail.getQuantity());
                    productDetailRepository.save(productDetail);
                }
                // Xóa BillDetail
                billDetailsRepository.delete(billDetail);
            }
        }
        billRepository.delete(bill);
        return bill;
    }

    @Override
    public BillResponseTH create(Employee employee) {
        Bill bill = new Bill();
        bill.setCode("HD" + Integer.parseInt(Long.toString(System.currentTimeMillis()).substring(7)));
        bill.setCreatedAt(new Date());
        bill.setPaymentMethod(paymentMethodRepository.findByNameIgnoreCase("Tiền mặt"));
        bill.setTypeBill(1);
        bill.setStatus(20);
        bill.setEmployee(employee);
        bill.setTotalAmount(BigDecimal.valueOf(0));
        return setBillResponse(billRepository.save(bill));
    }

    @Override
    public BillResponseTH update(Employee employee, BillResponseTH billRequest) {
        Optional<Bill> billOptional = billRepository.findById(billRequest.getId());
        if (billOptional.isEmpty()) {
            return null;
        }
        Bill bill = billOptional.get();
        bill.setEmployee(employee);
        bill.setCustomer(billRequest.getCustomer());
        Optional<PaymentMethod> paymentMethodOptional = paymentMethodRepository.findById(billRequest.getPaymentMethod().getId());
        if (paymentMethodOptional.isEmpty()) {
            return null;
        }
        bill.setPaymentMethod(paymentMethodOptional.get());
        return setBillResponse(billRepository.save(bill));
    }

    @Override
    public BillResponseTH paymentBill(Employee employee, BillResponseTH billRequest) {
        Optional<Bill> billOptional = billRepository.findById(billRequest.getId());
        if (billOptional.isEmpty()) {
            return null;
        }
        Bill bill = billOptional.get();
        bill.setCustomer(billRequest.getCustomer());
        bill.setEmployee(employee);
        Optional<PaymentMethod> paymentMethodOptional = paymentMethodRepository.findById(billRequest.getPaymentMethod().getId());
        if (paymentMethodOptional.isEmpty()) {
            return null;
        }
        bill.setStatus(21);
        bill.setPaidAmount(billRequest.getTotalAmountAfterDiscount());
        bill.setPaidShippingFee(billRequest.getShippingFee() != null ? bill.getShippingFee() : BigDecimal.valueOf(0));
        bill.setPaymentMethod(paymentMethodOptional.get());
        bill.setTotalAmountAfterDiscount(billRequest.getTotalAmountAfterDiscount());
        total(bill);
        Bill bill2 = billRepository.save(bill);

        if (bill2.getPaymentMethod().getName().equals("Tiền mặt")) {
            PaymentStatus paymentStatus = new PaymentStatus();
            paymentStatus.setBill(bill2);
            paymentStatus.setCustomerPaymentStatus(2);  
            paymentStatus.setPaymentMethod(1);
            paymentStatus.setPaymentType(1);
            paymentStatus.setPaymentAmount(bill2.getTotalAmountAfterDiscount().add(bill2.getShippingFee()));
            paymentStatusRepository.save(paymentStatus);
        } else {
            PaymentStatus paymentStatus = new PaymentStatus();
            paymentStatus.setBill(bill2);
            paymentStatus.setCustomerPaymentStatus(2);
            paymentStatus.setPaymentMethod(2);
            paymentStatus.setPaymentType(1);
            paymentStatus.setPaymentAmount(bill2.getTotalAmountAfterDiscount().add(bill2.getShippingFee()));
            paymentStatusRepository.save(paymentStatus);
        }

        BillHistory billHistory = new BillHistory();
        billHistory.setBill(bill2);
        billHistory.setStatus(20);
        billHistory.setType(1);
        billHistory.setCreatedAt(new Date());
        billHistory.setCreatedBy(employee.getFullName());
        BillHistory billHistory2 = new BillHistory();
        billHistory2.setBill(bill2);
        billHistory2.setStatus(21);
        billHistory2.setType(1);
        billHistory2.setCreatedAt(new Date());
        billHistory2.setCreatedBy(employee.getFullName());
        billHistoryRepository.save(billHistory);
        billHistoryRepository.save(billHistory2);
        return setBillResponse(bill2);
    }
}
