package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.advice.DuplicateException;
import com.example.demo.entity.*;
import com.example.demo.model.response.thuong.BillResponseTH;
import com.example.demo.repository.thuong.*;
import com.example.demo.service.thuong.BillServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
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

    @Autowired
    private VoucherRepositoryTH voucherRepository;

    private static final Random random = new Random();
    private static final String PREFIX = "TT";
    private static final int MAX_ATTEMPTS = 1000;

    public String generateUniqueCode() {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            int randomNumber = random.nextInt(10000);
            String code = PREFIX + String.format("%04d", randomNumber);

            if (!paymentStatusRepository.existsByCode(code)) {
                return code;
            }
        }
        throw new RuntimeException("Không thể tạo mã duy nhất sau " + MAX_ATTEMPTS + " lần thử.");
    }

    @Override
    public List<BillResponseTH> findAllByStatusAndTypeBill(Integer status, Integer typeBill) {
        // If you want to fetch for both typeBill = 1 and typeBill = 2
        List<Integer> typeBills = Arrays.asList(1, 2);

        return billRepository.findAllByStatusAndTypeBill(status, typeBills).stream().map(b -> {
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
            return null; // Bill không tồn tại
        }

        Bill bill = billOptional.get();

        // Tìm BillDetail theo Bill ID và ProductDetail ID
        Optional<BillDetail> billDetailOptional = billDetailsRepository.findByBill_IdAndProductDetail_Id(bill.getId(), id);

        BillDetail bd;
        if (billDetailOptional.isPresent()) {
            // Nếu BillDetail đã tồn tại, lấy nó ra
            bd = billDetailOptional.get();
            bd.setProductDetail(checkProductDetail(id));
            bd.setQuantity(bd.getQuantity() + 1); // Cộng số lượng
        } else {
            // Nếu BillDetail chưa tồn tại, tạo mới
            bd = new BillDetail();
            bd.setBill(bill);
            bd.setProductDetail(checkProductDetail(id)); // Kiểm tra và lấy ProductDetail
            bd.setQuantity(1); // Khởi tạo số lượng là 1
        }

        // Cập nhật các thông tin còn lại của BillDetail
        bd.setPrice(bd.getProductDetail().getProduct().getPrice());
        Integer promotionalPrice = productRepository.findPromotionalPriceByProductId(bd.getProductDetail().getProduct().getId());
        bd.setPromotionalPrice(promotionalPrice != null ? BigDecimal.valueOf(promotionalPrice) : bd.getProductDetail().getProduct().getPrice());

        // Thêm hoặc cập nhật BillDetail vào danh sách BillDetail của Bill
        if (bill.getBillDetail() == null) {
            bill.setBillDetail(new ArrayList<>());
        }
        bill.getBillDetail().removeIf(detail -> detail.getProductDetail().getId().equals(bd.getProductDetail().getId()));
        bill.getBillDetail().add(bd);

        total(bill); // Cập nhật tổng hóa đơn
        return setBillResponse(billRepository.save(bill)); // Lưu Bill và trả về response
    }

    @Override
    public BillResponseTH removeProductCart(BillResponseTH billRequest, Long id) {
        Optional<Bill> billOptional = billRepository.findById(billRequest.getId());
        if (billOptional.isEmpty()) {
            return null;
        }
        Bill bill = billOptional.get();

        Optional<BillDetail> billDetail = billDetailsRepository.findByBill_IdAndProductDetail_Id(bill.getId(),id);
        if (!billDetail.isPresent()){
            throw new RuntimeException("Bill not found with id " + id);
        }
        BillDetail bd = billDetail.get();

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

        Optional<BillDetail> billDetail = billDetailsRepository.findByBill_IdAndProductDetail_Id(bill.getId(),id);
        if (!billDetail.isPresent()){
            throw new RuntimeException("Bill not found with id " + id);
        }
        BillDetail bd = billDetail.get();
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

        Optional<BillDetail> billDetail = billDetailsRepository.findByBill_IdAndProductDetail_Id(bill.getId(),id);
        if (!billDetail.isPresent()){
            throw new RuntimeException("Bill not found with id " + id);
        }
        BillDetail bd = billDetail.get();

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
        // Fetch the Bill entity using the provided ID
        Optional<Bill> billOptional = billRepository.findById(billRequest.getId());
        if (billOptional.isEmpty()) {
            return null; // Return null if the bill is not found
        }

        Bill bill = billOptional.get();
        // Set the status based on the type of bill
        int newStatus = (billRequest.getTypeBill() == 1) ? 21 : (billRequest.getTypeBill() == 2) ? 1 : bill.getStatus();
        bill.setStatus(newStatus);

// Update voucher if provided
        if (billRequest.getVoucher() != null) {
            Optional<Voucher> newVoucherOptional = voucherRepository.findById(billRequest.getVoucher().getId());
            if (!newVoucherOptional.isPresent()) {
                throw new RuntimeException("Voucher not found");
            }
            Voucher newVoucher = newVoucherOptional.get();
            bill.setVoucher(newVoucher);
        } else {
            bill.setVoucher(null); // Optionally handle the case where no voucher is provided by clearing the existing voucher
        }


        // Set other fields of the Bill entity
        bill.setCustomer(billRequest.getCustomer());
        bill.setEmployee(employee);
        bill.setTotalAmountAfterDiscount(billRequest.getTotalAmountAfterDiscount());

        // Handle payment amounts based on the payment method
        if (bill.getPaymentMethod().getCode() == 10) {
            // Payment method code 10: Set paid amounts to zero
            bill.setPaidAmount(BigDecimal.valueOf(0));
            bill.setPaidShippingFee(BigDecimal.valueOf(0));
        } else if (bill.getPaymentMethod().getCode() == 13 || bill.getPaymentMethod().getCode() == 14) {
            // Payment method codes 13 or 14: Calculate paid amounts based on total amount after discount and shipping fee
            bill.setPaidAmount(bill.getTotalAmountAfterDiscount().add(
                    bill.getShippingFee() != null ? bill.getShippingFee() : BigDecimal.valueOf(0)
            ));
            bill.setPaidShippingFee(bill.getShippingFee() != null ? bill.getShippingFee() : BigDecimal.valueOf(0));
        } else {
            // Other payment methods: Set paid amounts based on total amount after discount and shipping fee
            bill.setPaidAmount(bill.getTotalAmountAfterDiscount().add(
                    bill.getShippingFee() != null ? bill.getShippingFee() : BigDecimal.valueOf(0)
            ));
            bill.setPaidShippingFee(bill.getShippingFee() != null ? bill.getShippingFee() : BigDecimal.valueOf(0));
        }

        bill.setPaymentMethod(billRequest.getPaymentMethod());

        // Save the updated Bill entity
        Bill savedBill = billRepository.save(bill);

        // Handle payment status if the payment method is provided and valid
        if (savedBill.getPaymentMethod().getCode() != 10) {
            PaymentStatus paymentStatus = new PaymentStatus();
            String paymentMethodName = savedBill.getPaymentMethod().getName();

            // Set the payment method type
            paymentStatus.setCode(generateUniqueCode());
            paymentStatus.setPaymentMethod(paymentMethodName.equals("Tiền mặt") ? 1 : paymentMethodName.equals("Chuyển khoản") ? 2 : null);
            paymentStatus.setBill(savedBill);
            paymentStatus.setCustomerPaymentStatus(2);
            paymentStatus.setPaymentType(1);

            paymentStatus.setPaymentAmount(savedBill.getTotalAmountAfterDiscount().add(
                    savedBill.getShippingFee() != null ? savedBill.getShippingFee() : BigDecimal.valueOf(0)
            ));

            paymentStatusRepository.save(paymentStatus);
        }

        // Create and save BillHistory entities
        BillHistory billHistory1 = createBillHistory(savedBill, employee);
        BillHistory billHistory2 = createBillHistory(savedBill, employee, newStatus);

        billHistoryRepository.save(billHistory1);
        billHistoryRepository.save(billHistory2);

        // Return the response
        return setBillResponse(savedBill);
    }


    private BillHistory createBillHistory(Bill bill, Employee employee) {
        return createBillHistory(bill, employee, 20);
    }

    private BillHistory createBillHistory(Bill bill, Employee employee, int status) {
        BillHistory billHistory = new BillHistory();
        billHistory.setBill(bill);
        billHistory.setType(1);
        billHistory.setStatus(status);
        billHistory.setCreatedAt(new Date());
        billHistory.setCreatedBy(employee.getFullName());
        return billHistory;
    }


    @Override
    public Bill updateBill(Long id, String address, String addressId, String reciverName,  String phoneNumber) {
        Optional<Bill> optionalBill = billRepository.findById(id);
        if (optionalBill.isPresent()) {
            Bill bill = optionalBill.get();
            bill.setAddress(address);
            bill.setAddressId(addressId);
            bill.setReciverName(reciverName);
            bill.setPhoneNumber(phoneNumber);

            return billRepository.save(bill); // Save the updated bill
        } else {
            throw new RuntimeException("Bill not found with id " + id);
        }
    }

    @Override
    public Bill updateShippingFee(Long id, BigDecimal shippingFee) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bill ID"));
        bill.setShippingFee(shippingFee);

        return billRepository.save(bill);
    }

    @Override
    public Bill updateTypeBill(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public Bill updatePaidAmount(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public Bill updateVoucher(Long billId, Long newVoucherId) {
        // Retrieve the Bill entity
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (!billOptional.isPresent()) {
            throw new RuntimeException("Bill not found");
        }
        Bill bill = billOptional.get();

        // Retrieve the new Voucher entity
        Optional<Voucher> newVoucherOptional = voucherRepository.findById(newVoucherId);
        if (!newVoucherOptional.isPresent()) {
            throw new RuntimeException("Voucher not found");
        }
        Voucher newVoucher = newVoucherOptional.get();
        // Check if the bill already has a voucher
        Voucher currentVoucher = bill.getVoucher();

        if (currentVoucher != null) {
            // Add back the quantity to the current voucher
            currentVoucher.setQuantity(currentVoucher.getQuantity() + 1);
            voucherRepository.save(currentVoucher);
        }

        // Subtract the quantity from the new voucher
        newVoucher.setQuantity(newVoucher.getQuantity() - 1);
        voucherRepository.save(newVoucher);

        // Set the new voucher to the bill
        bill.setVoucher(newVoucher);
        return billRepository.save(bill);
    }

    @Override
    public Bill removeVoucherFromBill(Long billId) {
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (billOptional.isEmpty()) {
            throw new RuntimeException("Bill not found with id " + billId);
        }
        Bill bill = billOptional.get();

        // Nếu bill đã có voucher, tăng số lượng lại cho voucher cũ
        if (bill.getVoucher() != null) {
            Voucher voucher = bill.getVoucher();
            voucher.setQuantity(voucher.getQuantity() + 1);
            voucherRepository.save(voucher);
        }

        // Xóa voucher khỏi bill
        bill.setVoucher(null);


        // Lưu lại thay đổi
        return billRepository.save(bill);
    }
}
