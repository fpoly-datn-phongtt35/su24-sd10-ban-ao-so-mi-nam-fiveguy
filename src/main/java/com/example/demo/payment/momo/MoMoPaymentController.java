package com.example.demo.payment.momo;

import com.example.demo.config.Config;
import com.example.demo.entity.Bill;
import com.example.demo.entity.Cart;
import com.example.demo.payment.momo.config.Environment;
import com.example.demo.payment.momo.models.QueryStatusTransactionResponse;
import com.example.demo.payment.momo.processor.QueryTransactionStatus;

import com.example.demo.restController.onlineShop.OLBillController2;
import com.example.demo.service.onlineShop.OLBillService2;
import com.example.demo.service.onlineShop.OLCartDetailService2;
import com.example.demo.service.onlineShop.OLCartService2;
import com.example.demo.service.onlineShop.impl.OlBillUntility;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/ol")
public class MoMoPaymentController {

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




    @PostMapping("/query-transaction")
    public ResponseEntity<QueryStatusTransactionResponse> queryTransactionStatus() {
                Environment environment = Environment.selectEnv("dev");

        try {
            QueryStatusTransactionResponse response = QueryTransactionStatus.process(environment, "MzE220240120174350", "MzE2");
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }









    //Momo
    @Transactional
    @GetMapping("/payment-momo/success")
    public void handleMoMoIPN2(@RequestParam("transId") String transId,@RequestParam("resultCode") String code,@RequestParam("requestId") String orderId, HttpServletResponse response, HttpSession session) throws IOException {
        Bill bill = olBillService.findById(olBillUntility.decodeId(orderId));

        if(code.equals("0") ){
            if (bill.getCustomer() != null){
                Cart cart = olCartService.findByCustomerId(bill.getCustomer().getId());

                if (cart != null) {
                    olCartDetailService.deleteAllByCart_Id(cart.getId());
                }
            }else {
                billRestController.setCheckOutBill(true);

            }
//            bill.setPaymentDate(new Date());
            bill.setStatus(1);
            olBillUntility.newPaymentStatusAndBillHistory(bill,bill.getCustomer(),1,2);

            bill.setTransId(transId);
            olBillService.save(bill);
            response.sendRedirect(Config.fe_liveServer_Success);

        }else {
            bill.setStatus(6);

//            olBillUntility.restoreProductQuantity(bill.getBillDetail());
//            if (bill.getVoucher() != null){
//                olBillUntility.increaseVoucherQuantity(bill.getVoucher().getId());
//            }
            olBillService.save(bill);

            response.sendRedirect(Config.fe_liveServer_Failed);
        }
    }


}