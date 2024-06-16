package com.example.demo.service.Customer.ServiceImpl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.ProductDetail;
import com.example.demo.repository.Customer.BillRepository;
import com.example.demo.service.Customer.BillService;
import com.example.demo.service.Customer.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

//    @Autowired
//    ProductDetailService productDetailService;

//    @Autowired
//    ProductService productService;

//    @Autowired
//    BillDetailRepository billDetailRepository;

    @Override
    public List<Bill> getAllBill() {
        return billRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Bill getBillById(Long id) {
        return billRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Bill> getAllBillPage(Integer page) {
        Pageable pageable = PageRequest.of(page, 1);
        return billRepository.findAll(pageable);
    }

    @Override
    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public Bill updateBill(Bill bill, Long id) {
        Optional<Bill> existingBill = billRepository.findById(id);
        if (existingBill.isPresent()){
            Bill bill1 = existingBill.get();
            bill1.setCode(bill.getCode());
            bill1.setCreatedAt(bill.getCreatedAt());
            bill1.setPaymentDate(bill.getPaymentDate());
            bill1.setTotalAmount(bill.getTotalAmount());
            bill1.setTotalAmountAfterDiscount(bill.getTotalAmountAfterDiscount());
            bill1.setReciverName(bill.getReciverName());
            bill1.setDeliveryDate(bill.getDeliveryDate());
            bill1.setShippingFee(bill.getShippingFee());
            bill1.setAddress(bill.getAddress());
            bill1.setPhoneNumber(bill.getPhoneNumber());
            bill1.setNote(bill.getNote());
//            bill1.setCustomerEntity(bill.getCustomerEntity());
            bill1.setEmployee(bill.getEmployee());
            bill1.setPaymentMethod(bill.getPaymentMethod());
            bill1.setVoucher(bill.getVoucher());
            bill1.setStatus(bill.getStatus());
            return billRepository.save(bill1);
        }else{
            throw new IllegalArgumentException("Không tìm thấy bill với Id "+ id);
        }
    }

    @Override
    public void deleteBill(Long id) {
        if (billRepository.existsById(id)){
            billRepository.deleteById(id);
        }else{
            throw new IllegalArgumentException("Không tìm thấy bill với Id "+ id);
        }
    }

    @Override
    public Bill updateStatus(Integer status, Long id) {
        Optional<Bill> existingBill = billRepository.findById(id);
        if (existingBill.isPresent()){
            Bill bill1 = existingBill.get();
            bill1.setStatus(status);
            if(bill1.getPaymentMethod().getName().equals("COD") && status == 3){
                bill1.setPaymentDate(new Date());
            }
            if(status == 2){
                bill1.setDeliveryDate(new Date());
            }
//            if(status == 4){
//                List<BillDetail> billDetails = billDetailRepository.findAllByBillId(bill1.getId());
//                for (BillDetail bd: billDetails) {
//                    int quantity = bd.getQuantity();
//                    ProductDetail pd = productDetailService.getById(bd.getProductDetail().getId());
//                    pd.setStatus(1);
//                    pd.setQuantity(pd.getQuantity() + quantity);
//                    productDetailService.update(pd, pd.getId());
//                    Product p = productService.getById(pd.getProduct().getId());
//                    p.setStatus(1);
//                    productService.update(p, p.getId());
//                    productDetailService.updateStatus(1, pd.getId());
//                }
//            }
            return billRepository.save(bill1);
        }else{
            throw new IllegalArgumentException("Không tìm thấy bill với Id "+ id);
        }
    }

    @Override
    public List<Bill> getAllExportExcel() {
        return billRepository.findAll();
    }

//OL

    @Autowired
    private BillRepository olProductDetailRepository;

//    @Autowired
//    private BillDetailRepository olBillDetailRepository;

//    @Autowired
//    private BillDetailService olBillDetailService;

//    @Autowired
//    private ProductDetailService olProductDetailService;

//    @Autowired
//    private VouchersRepository olVouchersRepository;

    @Autowired
    private BillRepository olBillRepository;

    @Autowired
    private RatingService olRatingService;

//    @Autowired
//    private ProductService olProductService;


//    public BillDetailResponse convertToOlBillDetailResponse(BillDetail billDetail) {
//        BillDetailResponse response = new BillDetailResponse();
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

//    private void updateProductQuantity(BillDetail billDetail) {
//        Optional<ProductDetail> productDetail = ProductDetailService.findById(billDetail.getProductDetail().getId());
//        if (productDetail.isPresent()){
//            int quantityToRemove = billDetail.getQuantity();
//            if (isQuantityAvailable(productDetail.get(), quantityToRemove)) {
//                int currentQuantity = productDetail.get().getQuantity();
//                productDetail.get().setQuantity(currentQuantity - quantityToRemove);
//
//                // Kiểm tra xem số lượng của chi tiết sản phẩm đã hết hay chưa
//                if (currentQuantity - quantityToRemove == 0) {
//                    productDetail.get().setStatus(2);  // Đặt status = 2 nếu số lượng hết
//                }
//
//                ProductDetailService.save(productDetail.get());
//
//                // Kiểm tra tất cả ProductDetail của Product có status = 2 không
//                checkAndUpdateProductStatus(productDetail.get().getProduct());
//            } else {
//                throw new IllegalArgumentException("Sản phẩm: " + productDetail.get().getProduct().getName() + " "
//                        + productDetail.get().getProduct().getCategory().getName() + " "
//                        + productDetail.get().getProduct().getMaterial().getName() + " " +
//                        productDetail.get().getProduct().getCode() + " Màu sắc: "
//                        + productDetail.get().getColor().getName() + " Kích cỡ: "
//                        + productDetail.get().getSize().getName() + " ");
//            }
//        }
//    }

//    private void checkAndUpdateProductStatus(Product product) {
//        List<ProductDetail> productDetails = ProductDetailService.findAllByProduct(product);
//        boolean allProductDetailsAreStatus2 = productDetails.stream().allMatch(pd -> pd.getStatus() == 2);
//
//        if (allProductDetailsAreStatus2) {
//            product.setStatus(2);  // Đặt status = 2 nếu tất cả ProductDetail đều có status = 2
//            ProductService.save(product);
//        }
//    }



    private boolean isQuantityAvailable(ProductDetail productDetail, int quantityToRemove) {
        int currentQuantity = productDetail.getQuantity();
        return currentQuantity >= quantityToRemove;
    }


//    @Override
//    public ResponseEntity<?> TaoHoaDonNguoiDungChuaDangNhap(JsonNode orderData) {
//        if (orderData == null) {
//            return ResponseEntity.ok(0);
//        }
//
//        ObjectMapper mapper = new ObjectMapper();
//        Bill bill = mapper.convertValue(orderData, Bill.class);
//
//// Kiểm tra số lượng tồn của voucher trước khi sử dụng
//        if (bill.getVoucher() != null){
//            Vouchers existingVoucher = VouchersRepository.findById(bill.getVoucher().getId())
//                    .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));
//
//            if (existingVoucher != null && existingVoucher.getStatus() == 1 && existingVoucher.getQuantity() > 0) {
//                existingVoucher.setQuantity(existingVoucher.getQuantity() - 1);
//
//                // Kiểm tra xem số lượng của voucher đã hết hay chưa
//                if (existingVoucher.getQuantity() == 0) {
//                    existingVoucher.setStatus(2);  // Đặt status = 2 nếu số lượng hết
//                }
//
//                VouchersRepository.save(existingVoucher);
//            } else {
//                return ResponseEntity.ok(3);
//            }
//        }
//
//
//        // Kiểm tra và xử lý số lượng sản phẩm trước khi thanh toán
//        List<BillDetail> billDetails = mapper.convertValue(orderData.get("billDetail"), new TypeReference<List<BillDetail>>() {});
//        List<String> insufficientQuantityProducts = new ArrayList<>();
//
//        for (BillDetail detail : billDetails) {
//            try {
//                updateProductQuantity(detail);
//                detail.setBill(bill);
//            } catch (IllegalArgumentException e) {
//                insufficientQuantityProducts.add(e.getMessage());
//            }
//        }
//
//        if (!insufficientQuantityProducts.isEmpty()) {
//            // Trả về danh sách sản phẩm không đủ số lượng
//            return ResponseEntity.ok(Collections.singletonMap(2, insufficientQuantityProducts));
//        }
//
//        bill.setCreatedAt(new Date());
//        Bill savedBill = olProductDetailRepository.save(bill);
//        BillDetailRepository.saveAll(billDetails);
//        return ResponseEntity.ok(savedBill);
//    }



    @Override
    public Page<Bill> findLatestBillsByCustomerId(Long customerId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return   olBillRepository.findLatestBillByCustomerId(customerId, pageRequest);
    }

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

//    private BillResponse mapBillToOlBillResponse(Bill bill) {
//        BillResponse olBillResponse = new BillResponse();
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
//        List<BillDetail> billDetails = BillDetailService.findByBill_IdAndStatus(bill.getId());
//        List<BillDetailResponse> responses = new ArrayList<>();
//
//        for (BillDetail billDetail : billDetails) {
//            BillDetailResponse response = convertToOlBillDetailResponse(billDetail);
//            List<Rating> list = olRatingService.findByBillDetail_Id(billDetail.getId());
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
//    public BillResponse findBYId(Long id) {
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

    @Override
    public List<Bill> findByPhoneNumber(String pn) {

        List<Bill> bill = olBillRepository.findByPhoneNumberOrderByCreatedAtDesc(pn);
        if (bill.size() > 0){
            return bill;
        }

        return Collections.emptyList();
    }
// END OL
}
