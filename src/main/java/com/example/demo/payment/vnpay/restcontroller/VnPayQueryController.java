package com.example.demo.payment.vnpay.restcontroller;

import com.example.demo.payment.vnpay.config.ConfigVNPay;
import com.example.demo.service.onlineShop.impl.OlBillUntility;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;

@RestController
@RequestMapping("/querydr")
public class VnPayQueryController {

    @PostMapping
    public ResponseEntity<String> doPost( @RequestParam("order_id") String orderId) throws Exception {
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest req = requestAttributes.getRequest();
            System.out.println(req);

            String vnp_RequestId = ConfigVNPay.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "querydr";
        String vnp_TmnCode = ConfigVNPay.vnp_TmnCode;
        String vnp_TxnRef = orderId;
        String vnp_OrderInfo = "Kiem tra ket qua GD OrderId:" + vnp_TxnRef;
//        String vnp_TransDate = transDate;

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

            String vnp_IpAddr = "192.168.1.1";

        JsonObject vnp_Params = new JsonObject();

        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", vnp_Version);
        vnp_Params.addProperty("vnp_Command", vnp_Command);
        vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
        String vnp_TransDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        vnp_Params.addProperty("vnp_TransactionDate", vnp_TransDate);
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);

        String hash_Data= String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode, vnp_TxnRef, vnp_TransDate, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);
        String vnp_SecureHash = ConfigVNPay.hmacSHA512(ConfigVNPay.secretKey, hash_Data.toString());

        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

        URL url = new URL(ConfigVNPay.vnp_ApiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "Application/json");
        con.setDoOutput(true);
        String postData = vnp_Params.toString();
        con.setRequestProperty("Content-Length", String.valueOf(postData.length()));

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postData);
        wr.flush();
        wr.close();
            System.out.println(req);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(vnp_Params.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(ConfigVNPay.vnp_ApiUrl, requestEntity, String.class);
        String responseBody = response.getBody();

        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);

        String transactionStatus = jsonObject.get("vnp_TransactionStatus").getAsString();

        System.out.println("vnp_TransactionStatus: " + transactionStatus);
        boolean isTransactionSuccessful = "00".equals(transactionStatus);

        return ResponseEntity.ok(Boolean.toString(isTransactionSuccessful));
    } catch (Exception e) {
        e.printStackTrace(); // Xử lý lỗi nếu cần
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process request: " + e.getMessage());
    }
    }

    @Autowired
    private OlBillUntility olBillUntility;

    @PostMapping("/refund")
    public ResponseEntity<String> refund( @RequestParam("order_id") String orderId  ,HttpServletRequest req) throws Exception {
        try {


            String codeBill = String.valueOf(olBillUntility.encodeId(Long.valueOf(orderId)));
            String vnp_RequestId = ConfigVNPay.getRandomNumber(8);
            String vnp_Version = "2.1.0";
            String vnp_Command = "refund";
            String vnp_TmnCode = ConfigVNPay.vnp_TmnCode;
            String vnp_TxnRef = codeBill;
            String vnp_OrderInfo = "KiemtraketquaGDOrderId:";
            String vnp_CreateBy = "Hai";

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());

            String vnp_IpAddr = "192.168.1.1";

            JsonObject vnp_Params = new JsonObject();
            vnp_Params.addProperty("vnp_TransactionNo", "14288950");
            vnp_Params.addProperty("vnp_TransactionType","02");
            vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
            vnp_Params.addProperty("vnp_Version", vnp_Version);
            vnp_Params.addProperty("vnp_Command", vnp_Command);
            vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
            String vnp_TransactionDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            vnp_Params.addProperty("vnp_TransactionDate", vnp_TransactionDate);
            vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
            vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.addProperty("vnp_CreateBy", vnp_CreateBy);
            vnp_Params.addProperty("vnp_Amount", String.valueOf(159957 * 100));

            String hash_Data = String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode, "02", vnp_TxnRef, String.valueOf(159957 * 100), "14288950", vnp_TransactionDate, vnp_CreateBy, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);
            System.out.println(hash_Data);
            String vnp_SecureHash = ConfigVNPay.hmacSHA512(ConfigVNPay.secretKey, hash_Data.toString());
            System.out.println(vnp_SecureHash);

            vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

            URL url = new URL(ConfigVNPay.vnp_ApiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "Application/json");
            con.setDoOutput(true);
            String postData = vnp_Params.toString();
            con.setRequestProperty("Content-Length", String.valueOf(postData.length()));

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postData);
            wr.flush();
            wr.close();

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(vnp_Params.toString(), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(ConfigVNPay.vnp_ApiUrl, requestEntity, String.class);
            String responseBody = response.getBody();
            System.out.println(response.getBody());

            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);

            String transactionStatus = jsonObject.get("vnp_TransactionStatus").getAsString();

            System.out.println("vnp_TransactionStatus: " + transactionStatus);
            boolean isTransactionSuccessful = "00".equals(transactionStatus);

            return ResponseEntity.ok(Boolean.toString(isTransactionSuccessful));
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý lỗi nếu cần
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process request: " + e.getMessage());
        }
    }


}
