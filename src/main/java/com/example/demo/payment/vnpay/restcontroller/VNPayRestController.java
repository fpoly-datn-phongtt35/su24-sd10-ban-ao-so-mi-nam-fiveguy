package com.example.demo.payment.vnpay.restcontroller;

import com.example.demo.config.Config;
import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.entity.Cart;
import com.example.demo.entity.ProductDetail;
import com.example.demo.payment.vnpay.DTO.PaymentRestDTO;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
import com.example.demo.payment.vnpay.config.ConfigVNPay;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.demo.restController.onlineShop.OLBillController2;

import com.example.demo.service.onlineShop.OLBillService2;
import com.example.demo.service.onlineShop.OLCartDetailService2;
import com.example.demo.service.onlineShop.OLCartService2;
import com.example.demo.service.onlineShop.impl.OlBillUntility;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/ol")
public class VNPayRestController {

    @Autowired
    private OLBillController2 billRestController;

    @Autowired
    private OLBillService2 olBillService;

    @Autowired
    private OLCartService2 olCartService;

    @Autowired
    private OLCartDetailService2 olCartDetailService;



    @Autowired
    private OlBillUntility olBillUntility;

//    @GetMapping("/payment-vnpay")
//    public ResponseEntity<?> getPay( HttpServletRequest req) throws UnsupportedEncodingException{
//
//        String vnp_Version = "2.1.0";
//        String vnp_Command = "pay";
//        String orderType = "other";
//        long amount = 100000*100;
//
//        String vnp_TxnRef = Config.getRandomNumber(8);
//        String vnp_IpAddr = Config.getIpAddress(req);
//
//        String vnp_TmnCode = Config.vnp_TmnCode;
//
//        Map<String, String> vnp_Params = new HashMap<>();
//        vnp_Params.put("vnp_Version", vnp_Version);
//        vnp_Params.put("vnp_Command", vnp_Command);
//        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//        vnp_Params.put("vnp_Amount", String.valueOf(amount));
//        vnp_Params.put("vnp_CurrCode", "VND");
//
//        vnp_Params.put("vnp_BankCode", "");
//        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
//        vnp_Params.put("vnp_OrderType", orderType);
//
//        vnp_Params.put("vnp_Locale", "vn");
//        vnp_Params.put("vnp_ReturnUrl",Config.vnp_ReturnUrl);
//        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//
//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnp_CreateDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//
//        cld.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
//
//        List fieldNames = new ArrayList(vnp_Params.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//        Iterator itr = fieldNames.iterator();
//        while (itr.hasNext()) {
//            String fieldName = (String) itr.next();
//            String fieldValue = (String) vnp_Params.get(fieldName);
//            if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                //Build hash data
//                hashData.append(fieldName);
//                hashData.append('=');
//                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                //Build query
//                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
//                query.append('=');
//                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                if (itr.hasNext()) {
//                    query.append('&');
//                    hashData.append('&');
//                }
//            }
//        }
//        String queryUrl = query.toString();
//        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
//        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//        String paymentUrlVNPAY = Config.vnp_PayUrl + "?" + queryUrl;
//
//        Map<String, String> jsonResponse = new HashMap<>();
//        jsonResponse.put("rederect", paymentUrlVNPAY);
//
//        Gson gson = new Gson();
//        String json = gson.toJson(jsonResponse);
//
//        System.out.println(paymentUrlVNPAY);
//
//        return ResponseEntity.ok(json);
//    }




//    private void restoreProductQuantity(List<BillDetail> billDetails) {
//        for (BillDetail detail : billDetails) {
//            Optional<ProductDetail> productDetail = olProductDetailService.findById(detail.getProductDetail().getId());
//            if (productDetail.isPresent()){
//                int quantityToAdd = detail.getQuantity();
//                int currentQuantity = productDetail.get().getQuantity();
//                productDetail.get().setQuantity(currentQuantity + quantityToAdd);
//                olProductDetailService.save(productDetail.get());
//            }
//        }
//    }

    @Transactional
    @GetMapping("/payment-vnpay/callback")
    public void paymentCallback(@RequestParam Map<String, String> queryParams, HttpServletResponse response,   HttpServletRequest  req) throws IOException {

        Bill bill = olBillService.findById(olBillUntility.decodeId(queryParams.get("vnp_TxnRef")));

        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
        String vnp_TransactionNo = queryParams.get("vnp_TransactionNo");
        System.out.println(vnp_TransactionNo);
            if ("00".equals(vnp_ResponseCode)) {
                if (bill.getCustomer() != null){
                    Cart cart = olCartService.findByCustomerId(bill.getCustomer().getId());

                    if (cart != null) {
                        olCartDetailService.deleteAllByCart_Id(cart.getId());
                    }
                }
//                else {
//                    billRestController.setCheckOutBill(true);
//
//                }
//                bill.setPaymentDate(new Date());
                bill.setStatus(1);
                olBillUntility.newPaymentStatusAndBillHistory(bill,bill.getCustomer(),1,2,1);
//                bill.setTransId(vnp_TransactionNo);

                olBillService.save(bill);

                response.sendRedirect(Config.fe_liveServer_Success);
            } else {
                bill.setStatus(6);
//                olBillUntility.restoreProductQuantity(bill.getBillDetail());
//                if (bill.getVoucher() != null){
//                    olBillUntility.increaseVoucherQuantity(bill.getVoucher().getId());
//                }
                olBillService.save(bill);

                response.sendRedirect(Config.fe_liveServer_Failed);

            }
    }







    @PostMapping("/querydr")
    public ResponseEntity<String> queryDR(@RequestParam("requestId") String request,@RequestParam("vnp_SecureHash") String vnp_SecureHash, HttpServletRequest req) {
        RestTemplate restTemplate = new RestTemplate();
        String vnPayUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

        // Build parameters for querydr request
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_RequestId", String.valueOf(System.currentTimeMillis()));
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "querydr");
        vnp_Params.put("vnp_TmnCode", ConfigVNPay.vnp_TmnCode);
        vnp_Params.put("vnp_TxnRef", request); // Use the transaction reference sent in the request
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + request);


        String vnp_IpAddr = ConfigVNPay.getIpAddress(req);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);


        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        String vnp_TransactionDate = vnp_CreateDate; // Sử dụng thời gian của vnp_CreateDate
        vnp_Params.put("vnp_TransactionDate", vnp_TransactionDate);
        // Calculate vnp_SecureHash
//        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        for (String fieldName : fieldNames) {
//            String fieldValue = vnp_Params.get(fieldName);
//            if (fieldValue != null && !fieldValue.isEmpty()) {
//                hashData.append(fieldName).append('=').append(fieldValue).append('&');
//            }
//        }
//        hashData.deleteCharAt(hashData.length() - 1);
//
//        String vnp_SecureHash = ConfigVNPay.hmacSHA512(ConfigVNPay.secretKey, hashData.toString());

        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);

        // Build query string
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
            query.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
        }
        query.deleteCharAt(query.length() - 1);

        // Send POST request to VNPAY
        ResponseEntity<String> response = restTemplate.postForEntity(vnPayUrl, query.toString(), String.class);

        return response;
    }


//    @PostMapping("/2")
//    public ResponseEntity<String> queryDR(HttpServletRequest req,@RequestParam("order_id") String orderId,
//                                          @RequestParam("trans_date") String transDate) {
//
//        String vnp_RequestId = String.valueOf(System.currentTimeMillis());
//        String vnp_Version = "2.1.0";
//        String vnp_Command = "querydr";
//        String vnp_TmnCode = "9MSU0G61";
//        String vnp_TxnRef = orderId;
//        String vnp_OrderInfo = "Kiem tra ket qua GD OrderId:" + vnp_TxnRef;
//        String vnp_TransDate = transDate;
//
//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnp_CreateDate = formatter.format(cld.getTime());
//        String vnp_IpAddr = ConfigVNPay.getIpAddress(req);
//
//        JsonObject  vnp_Params = new JsonObject ();
//
//        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
//        vnp_Params.addProperty("vnp_Version", vnp_Version);
//        vnp_Params.addProperty("vnp_Command", vnp_Command);
//        vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
//        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
//        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
//        //vnp_Params.put("vnp_TransactionNo", vnp_TransactionNo);
//        vnp_Params.addProperty("vnp_TransactionDate", vnp_TransDate);
//        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
//        vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);
//
//        String hash_Data = String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode, vnp_TxnRef, vnp_TransDate, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);
//        String vnp_SecureHash = ConfigVNPay.hmacSHA512(ConfigVNPay.secretKey, hash_Data.toString());
//
//        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);
//
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // Build JSON object with vnp_Params
//        JsonObject vnp_Params = new JsonObject();
//        // Populate vnp_Params with necessary parameters
//
//        HttpEntity<String> requestEntity = new HttpEntity<>(vnp_Params.toString(), headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(ConfigVNPay.vnp_ApiUrl, requestEntity, String.class);
//
//        // Process the response from VNPAY
//
//        return response;
//    }




}
