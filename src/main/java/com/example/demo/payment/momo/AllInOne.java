package com.example.demo.payment.momo;//package com.example.demo.payment.momo;
//
//import com.example.demo.entity.Bill;
//import com.example.demo.payment.momo.config.Environment;
//import com.example.demo.payment.momo.enums.ConfirmRequestType;
//import com.example.demo.payment.momo.enums.RequestType;
//import com.example.demo.payment.momo.models.ConfirmResponse;
//import com.example.demo.payment.momo.models.PaymentResponse;
//import com.example.demo.payment.momo.processor.ConfirmTransaction;
//import com.example.demo.payment.momo.processor.CreateOrderMoMo;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//
///**
// * Demo
// */
//public class AllInOne {
//
//    /***
//     * Select environment
//     * You can load config from file
//     * MoMo only provide once endpoint for each envs: dev and prod
//     * @param args
//     * @throws
//     */
//
//    public static void main(String... args) throws Exception {
////        LogUtils.init();
//        String requestId = String.valueOf(System.currentTimeMillis());
//        String orderId = String.valueOf(System.currentTimeMillis());
//        Long transId = 2L;
//        long amount = 50000;
//
//        String partnerClientId = "partnerClientId";
//        String orderInfo = "Pay With MoMo";
//        String redirectUrl = "https://google.com.vn";
//        String ipnUrl = "http://localhost:8080/momo-ipn";
//        String callbackToken = "callbackToken";
//        String token = "";
//
//        Environment environment = Environment.selectEnv("dev");
//
//        PaymentResponse captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(amount), orderInfo, redirectUrl, ipnUrl, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);
//
//        /***
//         * confirm transaction with Momo's ATM type
//         */
//
//
//        ConfirmResponse confirmResponse = ConfirmTransaction.process(environment, orderId, requestId, Long.toString(amount), ConfirmRequestType.CAPTURE, "");
//
//    }
//
////    @GetMapping("/processPayment")
////    public PaymentResponse processPayment(@RequestParam("bill") Bill bill) {
////        try {
////            String requestId = String.valueOf(System.currentTimeMillis());
////            String orderId = String.valueOf(System.currentTimeMillis());
////            long amount = 50000;
////
////            String partnerClientId = "partnerClientId";
////            String orderInfo = "Pay With MoMo";
////            String returnURL = "https://google.com.vn";
////            String notifyURL = "https://google.com.vn";
////            String callbackToken = "callbackToken";
////            String token = "";
////
////            Environment environment = Environment.selectEnv("dev");
////            return CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(amount), orderInfo, returnURL, notifyURL, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);
////        } catch (Exception e) {
////            // Xử lý các trường hợp ngoại lệ
////            e.printStackTrace();
////            return null;
////        }
////    }
//
//}
