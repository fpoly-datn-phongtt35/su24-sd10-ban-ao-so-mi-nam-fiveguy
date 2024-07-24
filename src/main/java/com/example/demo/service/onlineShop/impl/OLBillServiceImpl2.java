package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.common.VoucherCommonRepository;
import com.example.demo.repository.onlineShop.OLBillRepository2;
import com.example.demo.service.onlineShop.OLBillDetailService2;
import com.example.demo.service.onlineShop.OLBillService2;
import com.example.demo.service.onlineShop.OLProductDetailService2;
import com.example.demo.service.onlineShop.OLProductService2;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service

public class OLBillServiceImpl2 implements OLBillService2 {


    @Autowired
    private OLBillDetailService2 olBillDetailService;

    @Autowired
    private OLProductDetailService2 olProductDetailService;

    @Autowired
    private VoucherCommonRepository olVouchersRepository;

    @Autowired
    private OLBillRepository2 olBillRepository;

//    @Autowired
//    private OlRatingService olRatingService;

    @Autowired
    private OLProductService2 olProductService;


//    public OlBillDetailResponse convertToOlBillDetailResponse(BillDetail billDetail) {
//        OlBillDetailResponse response = new OlBillDetailResponse();
//        response.setId(billDetail.getId());
//        response.setQuantity(billDetail.getQuantity());
//        response.setPrice(billDetail.getPrice());
//        response.setBill(billDetail.getBill());
//        response.setProductDetail(billDetail.getProductDetail());
//        response.setStatus(billDetail.getStatus());
//        List<RatingEntity> list = olRatingService.findByBillDetail_Id(billDetail.getId());
//
//        if (list.isEmpty() || (!list.get(0).isRated())) {
//            response.setRated(false); // Thiết lập thuộc tính 'rated' dựa trên điều kiện
//        } else {
//            response.setRated(true);
//        }
//
//        return response;
//    }

    private boolean isQuantityAvailable(ProductDetail productDetail, int quantityToRemove) {
        int currentQuantity = productDetail.getQuantity() - 1;
        return currentQuantity >= quantityToRemove;
    }

    private void updateProductQuantity(BillDetail billDetail) {
        Optional<ProductDetail> productDetail = olProductDetailService.findById(billDetail.getProductDetail().getId());
        if (productDetail.isPresent()){
            int quantityToRemove = billDetail.getQuantity();
            if (isQuantityAvailable(productDetail.get(), quantityToRemove)) {
//                int currentQuantity = productDetail.get().getQuantity();
//                productDetail.get().setQuantity(currentQuantity - quantityToRemove);
//
//                // Kiểm tra xem số lượng của chi tiết sản phẩm đã hết hay chưa
//                if (currentQuantity - quantityToRemove == 0) {
//                    productDetail.get().setStatus(2);  // Đặt status = 2 nếu số lượng hết
//                }
//
//                olProductDetailService.save(productDetail.get());

                // Kiểm tra tất cả ProductDetail của Product có status = 2 không
//                checkAndUpdateProductStatus(productDetail.get().getProduct());
            } else {
                throw new IllegalArgumentException("Sản phẩm: " + productDetail.get().getProduct().getName() + " "
                        + productDetail.get().getProduct().getCategory().getName() + " "
                        + productDetail.get().getProduct().getMaterial().getName() + " " +
                        productDetail.get().getProduct().getCode() + " Màu sắc: "
                        + productDetail.get().getColor().getName() + " Kích cỡ: "
                        + productDetail.get().getSize().getName() + " ");
            }
        }
    }

//    private void checkAndUpdateProductStatus(Product product) {
//        List<ProductDetail> productDetails = olProductDetailService.findAllByProduct(product);
//        boolean allProductDetailsAreStatus2 = productDetails.stream().allMatch(pd -> pd.getStatus() == 2);
//
//        if (allProductDetailsAreStatus2) {
//            product.setStatus(2);  // Đặt status = 2 nếu tất cả ProductDetail đều có status = 2
//            olProductService.save(product);
//        }
//    }






    @Override
    public ResponseEntity<?> creatBill(JsonNode orderData, Customer customer) {
        if (orderData == null) {
            return ResponseEntity.ok(0);
        }

        ObjectMapper mapper = new ObjectMapper();
        Bill bill = mapper.convertValue(orderData, Bill.class);

// Kiểm tra số lượng tồn của voucher trước khi sử dụng
        if (bill.getVoucher() != null){
            Voucher existingVoucher = olVouchersRepository.findById(bill.getVoucher().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));

            if (existingVoucher != null && existingVoucher.getStatus() == 1 && existingVoucher.getQuantity() > 0) {
//                existingVoucher.setQuantity(existingVoucher.getQuantity() - 1);
//
//                // Kiểm tra xem số lượng của voucher đã hết hay chưa
//                if (existingVoucher.getQuantity() == 0) {
//                    existingVoucher.setStatus(2);  // Đặt status = 2 nếu số lượng hết
//                }
//
//                olVouchersRepository.save(existingVoucher);
            } else {
                return ResponseEntity.ok(3);
            }
        }


        // Kiểm tra và xử lý số lượng sản phẩm trước khi thanh toán
        List<BillDetail> billDetails = mapper.convertValue(orderData.get("billDetail"), new TypeReference<List<BillDetail>>() {});
        List<String> insufficientQuantityProducts = new ArrayList<>();

        for (BillDetail detail : billDetails) {
            try {
                updateProductQuantity(detail);
                detail.setBill(bill);
            } catch (IllegalArgumentException e) {
                insufficientQuantityProducts.add(e.getMessage());
            }
        }

        if (!insufficientQuantityProducts.isEmpty()) {
            // Trả về danh sách sản phẩm không đủ số lượng
            return ResponseEntity.ok(Collections.singletonMap(2, insufficientQuantityProducts));
        }

//        bill.setCreatedAt(new Date());
        bill.setCustomer(customer);
        Bill savedBill = olBillRepository.save(bill);
        olBillDetailService.saveAll(billDetails);
        return ResponseEntity.ok(savedBill);
    }



//    @Override
//    public Page<Bill> findLatestBillsByCustomerId(Long customerId, int page, int size) {
//        PageRequest pageRequest = PageRequest.of(page, size);
//        return   olBillRepository.findLatestBillByCustomerId(customerId, pageRequest);
//    }

    @Override
    public boolean updatePaymentStatus(Long billId, int paymentStatus) {
        Optional<Bill> optionalBill = olBillRepository.findById(billId);
        if (optionalBill.isPresent()) {
            Bill bill = optionalBill.get();
            bill.setStatus(paymentStatus);
            olBillRepository.save(bill);
            return true;
        }
        return false;
    }

//    private OlBillResponse mapBillToOlBillResponse(Bill bill) {
//        OlBillResponse olBillResponse = new OlBillResponse();
//        olBillResponse.setId(bill.getId());
//        olBillResponse.setCode(bill.getCode());
//        olBillResponse.setCreatedAt(bill.getCreatedAt());
//        olBillResponse.setPaymentDate(bill.getPaymentDate());
//        olBillResponse.setTotalAmount(bill.getTotalAmount());
//        olBillResponse.setTotalAmountAfterDiscount(bill.getTotalAmountAfterDiscount());
//        olBillResponse.setReciverName(bill.getReciverName());
//        olBillResponse.setDeliveryDate(bill.getDeliveryDate());
//        olBillResponse.setShippingFee(bill.getShippingFee());
//        olBillResponse.setAddress(bill.getAddress());
//        olBillResponse.setPhoneNumber(bill.getPhoneNumber());
//        olBillResponse.setNote(bill.getNote());
//        olBillResponse.setCustomerEntity(bill.getCustomerEntity());
//        olBillResponse.setEmployee(bill.getEmployee());
//        olBillResponse.setPaymentMethod(bill.getPaymentMethod());
//        olBillResponse.setVoucher(bill.getVoucher());
//        olBillResponse.setTypeBill(bill.getTypeBill());
//        olBillResponse.setStatus(bill.getStatus());
//        olBillResponse.setReason(bill.getReason());
//
//
//
//        List<BillDetail> billDetails = olBillDetailService.findByBill_IdAndStatus(bill.getId());
//        List<OlBillDetailResponse> responses = new ArrayList<>();
//
//        for (BillDetail billDetail : billDetails) {
//            OlBillDetailResponse response = convertToOlBillDetailResponse(billDetail);
//            List<RatingEntity> list = olRatingService.findByBillDetail_Id(billDetail.getId());
//
//            if (list.isEmpty() || (!list.get(0).isRated())) {
//                response.setRated(false); // Thiết lập thuộc tính 'rated' dựa trên điều kiện
//            } else {
//                response.setRated(true);
//            }
//
//            responses.add(response);
//        }
//        olBillResponse.setBillDetail(responses);
//
//
//// responses là danh sách đã được chuyển đổi và thiết lập 'rated' tương ứng
//
//        return olBillResponse;
//    }

//    @Override
//    public OlBillDTO findBYId(Long id) {
//        Optional<Bill> bill = olBillRepository.findById(id);
//        if (bill.isPresent()){
//            return mapBillToOlBillResponse(bill.get());
//        }
//        return null;
//    }

    @Override
    public Bill save(Bill bill) {
        return olBillRepository.save(bill);
    }

    @Override
    public Bill findById(Long id) {
        Optional<Bill> bill = olBillRepository.findById(id);
        if (bill.isPresent()){
            return bill.get();
        }
        return null;
    }

//    @Override
//    public List<Bill> findByPhoneNumber(String pn) {
//
//        List<Bill> bill = olBillRepository.findByPhoneNumberOrderByCreatedAtDesc(pn);
//        if (bill.size() > 0){
//            return bill;
//        }
//
//        return Collections.emptyList();
//    }
}
