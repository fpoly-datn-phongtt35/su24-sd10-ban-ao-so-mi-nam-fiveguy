package com.example.demo.restController.nguyen;

import com.example.demo.service.nguyen.NReturnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/returnOrder")
public class NReturnOrderRestController {

    @Autowired
    private NReturnOrderService returnOrderService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getReturnOrderByBillId(@PathVariable Long id) {
        return ResponseEntity.ok(returnOrderService.findAllReturnOrdersByBillId(id));
    }

}
