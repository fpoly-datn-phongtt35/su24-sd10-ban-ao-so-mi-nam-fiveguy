package com.example.demo.restController.onlineShop;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.entity.BillHistory;
import com.example.demo.entity.Customer;
import com.example.demo.model.request.onlineShop.BillRequest2;
import com.example.demo.security.service.SCAccountService;
import com.example.demo.security.service.SCCustomerService;
import com.example.demo.service.onlineShop.OLBillDetailService2;
import com.example.demo.service.onlineShop.OLBillHistoryService2;
import com.example.demo.service.onlineShop.OLBillService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/home")
public class OLOrderController2 {

    @Autowired
    private OLBillService2 olBillService2;

    @Autowired
    private OLBillHistoryService2 olBillHistoryService2;

    @Autowired
    private SCCustomerService SCCustomerService;

    @Autowired
    private OLBillDetailService2 olBillDetailService2;

    @Autowired
    private SCAccountService accountService;

    @GetMapping("/order/customer")
    public Page<Bill> getBillsByCustomerId(@RequestHeader("Authorization") String token, @RequestParam(required = false, defaultValue = "") String search,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Optional<Customer> customer = SCCustomerService.getCustomerByToken(token);

        if (customer.isPresent()) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<Bill> bills = olBillService2.getBillsByCustomerId(customer.get().getId(), search, pageable);
            return bills;
        }
        return Page.empty();
    }

    @GetMapping("/order/phone")
    public Page<Bill> getBillsByPhoneNumber(@RequestParam String phoneNumber,
                                            @RequestParam(required = false, defaultValue = "") String search,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return olBillService2.getBillsByPhoneNumber(phoneNumber, search, page, size);
    }

    @GetMapping("/bill-history/bill/{billId}")
    public List<BillHistory> getBillHistoriesByBillId(@PathVariable Long billId) {
        return olBillHistoryService2.getBillHistoriesByBillId(billId);
    }

    @PutMapping("/bill/billStatusUpdate/{id}")
    public Bill updateBillStatusAndSaveBillHistory(
            @RequestHeader("Authorization") String token, @RequestBody BillRequest2 billRequest,
            @PathVariable Long id) {

            Optional<String> fullName = accountService.getFullNameByToken(token);
            billRequest.getBillHistory().setCreatedBy(fullName.get());

            return olBillService2.updateStatusAndBillStatus(billRequest.getBill(), id, billRequest.getBillHistory());




    }

    @GetMapping("/bill/{billId}")
    public Bill getBillById(@PathVariable Long billId) {
        return olBillService2.findById(billId);
    }

    @GetMapping("/bill/billDetail/{billId}")
    public List<BillDetail> getBillDetailByIdill(@PathVariable Long billId) {
        return olBillDetailService2.getBillDetailsByBillId(billId);
    }

}
