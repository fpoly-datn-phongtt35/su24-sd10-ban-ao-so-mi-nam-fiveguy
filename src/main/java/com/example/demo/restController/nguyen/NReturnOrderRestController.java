package com.example.demo.restController.nguyen;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.ReturnOrder;
import com.example.demo.model.response.nguyen.ReturnOrderResponse;
import com.example.demo.security.service.SCAccountService;
import com.example.demo.service.nguyen.NProductService;
import com.example.demo.service.nguyen.NReturnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/returnOrder")
public class NReturnOrderRestController {

    @Autowired
    private NReturnOrderService returnOrderService;

    @Autowired
    private NProductService productService;

    @Autowired
    private SCAccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getReturnOrderByBillId(@PathVariable Long id) {

        List<ReturnOrder> returnOrders = returnOrderService.findAllReturnOrdersByBillId(id);

        List<ReturnOrderResponse> responseList = returnOrders.stream()
                .map(returnOrder -> {
                    String imagePath = productService.getImagePathByProductId(
                            returnOrder.getBillDetail().getProductDetail().getProduct().getId(),
                            returnOrder.getBillDetail().getProductDetail().getColor().getId());
                    return toReturnOrderResponse(returnOrder, imagePath);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);

//        return ResponseEntity.ok(returnOrderService.findAllReturnOrdersByBillId(id));
    }

    @PostMapping("/addReturnOrder")
    public ResponseEntity<?> addReturnOrderAndUpdateBill(
            @RequestHeader("Authorization") String token,
            @RequestBody List<ReturnOrder> returnOrders) {
        Optional<String> fullName = accountService.getFullNameByToken(token);

        return ResponseEntity
                .ok(returnOrderService.addReturnOrderAndUpdateBill(returnOrders, fullName.get()));
    }

    @PutMapping("/{billId}/calculateSummary")
    public ResponseEntity<?> calculateReturnOrderSummary(@PathVariable Long billId,
            @RequestBody List<ReturnOrder> returnOrders) {

        return ResponseEntity
                .ok(returnOrderService.calculateReturnOrderSummary(billId, returnOrders));
    }

    @PutMapping("/{billId}/calculateSummaryBillDetail")
    public ResponseEntity<?> calculateBillDetailSummary(@PathVariable Long billId,
                                                         @RequestBody List<BillDetail> billDetails) {

        return ResponseEntity
                .ok(returnOrderService.calculateBillDetailSummary(billId, billDetails));
    }

    private ReturnOrderResponse toReturnOrderResponse(ReturnOrder returnOrder, String imagePath) {
        ReturnOrderResponse response = new ReturnOrderResponse();
        response.setId(returnOrder.getId());
        response.setQuantity(returnOrder.getQuantity());
        response.setType(returnOrder.getType()); // You can set the appropriate type if needed
        response.setBill(returnOrder.getBill());
        response.setBillDetail(returnOrder.getBillDetail());
        response.setReturnReason(returnOrder.getReturnReason()); // You can set the appropriate return reason if needed
        response.setReturnStatus(returnOrder.getReturnStatus()); // You can set the appropriate return status if needed
        response.setCreatedAt(returnOrder.getCreatedAt()); // Assuming current date as createdAt
        response.setUpdatedAt(returnOrder.getUpdatedAt()); // Assuming current date as updatedAt
        response.setImagePath(imagePath);
        return response;
    }

}
