package com.example.demo.restController.nguyen;

import com.example.demo.entity.ReturnOrder;
import com.example.demo.security.service.SCAccountService;
import com.example.demo.service.nguyen.NReturnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/returnOrder")
public class NReturnOrderRestController {

    @Autowired
    private NReturnOrderService returnOrderService;

    @Autowired
    private SCAccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getReturnOrderByBillId(@PathVariable Long id) {
        return ResponseEntity.ok(returnOrderService.findAllReturnOrdersByBillId(id));
    }

    @PostMapping("/addReturnOrder")
    public ResponseEntity<?> addReturnOrderAndUpdateBill(
            @RequestHeader("Authorization") String token,
            @RequestBody List<ReturnOrder> returnOrders) {
        Optional<String> fullName = accountService.getFullNameByToken(token);

        return ResponseEntity
                .ok(returnOrderService.addReturnOrderAndUpdateBill(returnOrders, fullName.get()));
    }

}
