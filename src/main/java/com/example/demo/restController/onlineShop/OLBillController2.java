package com.example.demo.restController.onlineShop;

import com.example.demo.entity.*;

import com.example.demo.payment.momo.config.Environment;
import com.example.demo.payment.momo.enums.RequestType;
import com.example.demo.payment.momo.models.PaymentResponse;
import com.example.demo.payment.momo.processor.CreateOrderMoMo;
import com.example.demo.payment.vnpay.config.ConfigVNPay;
import com.example.demo.security.service.SCCustomerService;
import com.example.demo.service.onlineShop.*;
import com.example.demo.service.onlineShop.impl.OlBillUntility;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/home")
public class OLBillController2 {

//    boolean checkOutBill = false;
//
//    public void setCheckOutBill(boolean checkOutBill) {
//        this.checkOutBill = checkOutBill;
//    }

    @Autowired
    private OLCartService2 olCartService;

    @Autowired
    private OlBillUntility olBillUntility;

    @Autowired
    private OLCartDetailService2 olCartDetailService;

    @Autowired
    private OLBillService2 olBillService;

    @Autowired
    private SCCustomerService SCCustomerService;


    @Transactional
    @PostMapping("/bill/create")
    public ResponseEntity<?> TaoHoaDonNguoiDungChuaDangNhap(@RequestBody JsonNode orderData, HttpServletRequest req, @RequestHeader("Authorization") String token) throws IOException {
        Optional<Customer> customer = SCCustomerService.getCustomerByToken(token);
        if (customer.isPresent()) {

//            System.out.println(orderData);

        ResponseEntity<?> newBill = olBillService.creatBill(orderData,customer.get());
        Object body = newBill.getBody();

        if (body != null && body instanceof Bill) {
            Bill billData = (Bill) body;
            BigDecimal totalPayment = new BigDecimal(String.valueOf(billData.getTotalAmountAfterDiscount()));
            Integer codePayment = billData.getPaymentMethod().getCode();
            String codeBill = (olBillUntility.encodeId(billData.getId()));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String timestamp = dateFormat.format(new Date());
            // Kết hợp idString với timestamp
            String orderId = codeBill + timestamp;

//            VNPAY
            if (codePayment.equals(11)) {
                olBillUntility.scheduleBillDeletion(billData.getId(), req);

                String vnp_Version = "2.1.0";
                String vnp_Command = "pay";
                String orderType = "other";
                String vnp_IpAddr = ConfigVNPay.getIpAddress(req);
                String vnp_TmnCode = ConfigVNPay.vnp_TmnCode;
                Map<String, String> vnp_Params = new HashMap<>();
                vnp_Params.put("vnp_Version", vnp_Version);
                vnp_Params.put("vnp_Command", vnp_Command);
                vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
                vnp_Params.put("vnp_Amount", String.valueOf(totalPayment.multiply(BigDecimal.valueOf(100))));
                vnp_Params.put("vnp_CurrCode", "VND");
                vnp_Params.put("vnp_BankCode", "");
                vnp_Params.put("vnp_TxnRef", codeBill);
                vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + codeBill);
                vnp_Params.put("vnp_OrderType", orderType);
                vnp_Params.put("vnp_Locale", "vn");
                vnp_Params.put("vnp_ReturnUrl", ConfigVNPay.vnp_ReturnUrl);
                vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
                Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                String vnp_CreateDate = formatter.format(cld.getTime());
                vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
                cld.add(Calendar.MINUTE, 15);
                String vnp_ExpireDate = formatter.format(cld.getTime());
                vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
                List fieldNames = new ArrayList(vnp_Params.keySet());
                Collections.sort(fieldNames);
                StringBuilder hashData = new StringBuilder();
                StringBuilder query = new StringBuilder();
                Iterator itr = fieldNames.iterator();
                while (itr.hasNext()) {
                    String fieldName = (String) itr.next();
                    String fieldValue = (String) vnp_Params.get(fieldName);
                    if ((fieldValue != null) && (fieldValue.length() > 0)) {
                        //Build hash data
                        hashData.append(fieldName);
                        hashData.append('=');
                        hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                        //Build query
                        query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                        query.append('=');
                        query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                        if (itr.hasNext()) {
                            query.append('&');
                            hashData.append('&');
                        }
                    }
                }
                String queryUrl = query.toString();
                String vnp_SecureHash = ConfigVNPay.hmacSHA512(ConfigVNPay.secretKey, hashData.toString());
                queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
                String paymentUrlVNPAY = ConfigVNPay.vnp_PayUrl + "?" + queryUrl;
                Map<String, String> jsonResponse = new HashMap<>();
                jsonResponse.put("redirect", paymentUrlVNPAY);
                Gson gson = new Gson();
                String json = gson.toJson(jsonResponse);
                return ResponseEntity.ok(json);
            }
//            MOMO

            else if (codePayment.equals(12))
            {
                olBillUntility.scheduleBillDeletion(billData.getId(), req);
                String orderInfo = "Thanh toán cho đơn hàng ";
                String redirectUrl = "http://localhost:8080/api/ol/payment-momo/success";
                String ipnUrl = "http://localhost:8080/api/ol/payment-momo/success";
                Environment environment = Environment.selectEnv("dev");
                try {
                    PaymentResponse captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, codeBill,String.valueOf(totalPayment), orderInfo, redirectUrl, ipnUrl, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);
                    Map<String, String> jsonResponse = new HashMap<>();
                    jsonResponse.put("redirect", captureWalletMoMoResponse.getPayUrl());
                    Gson gson = new Gson();
                    String json = gson.toJson(jsonResponse);
                    return ResponseEntity.ok(json);
                } catch (Exception e) {
                    // Xử lý lỗi nếu có
                    return ResponseEntity.ok(0);

                }
            }
//            COD
             else if (codePayment.equals(10)) {
                billData.setStatus(1);
                 billData.setCustomer(customer.get());
               Bill bill = olBillService.save(billData);
               olBillUntility.newPaymentStatusAndBillHistory(bill,customer.get(),1,1,0);
                    Cart cart = olCartService.findByCustomerId(customer.get().getId());
                    if (cart != null) {
                        olCartDetailService.deleteAllByCart_Id(cart.getId());
                    }
                return ResponseEntity.ok(333);
            }

        } else if (body instanceof Integer) {
            int intValue = (int) body;
            if (intValue == 3) {
                return ResponseEntity.ok(3);
            } else {
                return ResponseEntity.ok(0);
            }
        } else if (body instanceof Map) {
            // Kiểm tra xem body có key là 2 không
            Map<?, ?> bodyMap = (Map<?, ?>) body;
            if (bodyMap.containsKey(2)) {
                // Lấy danh sách sản phẩm không đủ số lượng
                List<String> insufficientQuantityProducts = (List<String>) bodyMap.get(2);
                // Tạo một đối tượng hoặc Map để chứa cả 2 giá trị
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("resultCode", 2);
                responseMap.put("insufficientQuantityProducts", insufficientQuantityProducts);
                // Trả về đối tượng hoặc Map chứa cả 2 giá trị
                return ResponseEntity.ok(responseMap);
            }
        }

        }

        return ResponseEntity.ok(0);


    }


//    @GetMapping("bill/checkThePayper")
//    public boolean checkThePayper() {
//        return checkOutBill;
//    }
//
//    @PutMapping("/bill/updateCheckoutStatus")
//    public void updateCheckoutStatus() {
//        checkOutBill = false;
//
//    }


}