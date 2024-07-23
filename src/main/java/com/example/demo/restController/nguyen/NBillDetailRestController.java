package com.example.demo.restController.nguyen;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.ProductDetail;
import com.example.demo.model.response.nguyen.BillDetailResponse;
import com.example.demo.model.response.nguyen.BillDetailSummary;
import com.example.demo.model.response.nguyen.ProductDetailResponse;
import com.example.demo.service.nguyen.NBillDetailService;
import com.example.demo.service.nguyen.NProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/billDetail")
public class NBillDetailRestController {

    @Autowired
    NBillDetailService billDetailService;

    @Autowired
    private NProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(billDetailService.getById(id));
    }

    @GetMapping("/getAllByBillId/{billId}")
    public ResponseEntity<?> getAllByBillId(@PathVariable Long billId) {

        List<BillDetail> billDetails = billDetailService.getAllByBillId(billId);

        List<BillDetailResponse> responseList = billDetails.stream()
                .map(billDetail -> {
                    String imagePath = productService.getImagePathByProductId(
                            billDetail.getProductDetail().getProduct().getId());
                    return toResponse(billDetail, imagePath);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{billId}/summary")
    public ResponseEntity<BillDetailSummary> getBillDetailSummary(@PathVariable Long billId) {
        BillDetailSummary summary = billDetailService.getBillDetailSummaryByBillId(billId);
        return ResponseEntity.ok(summary);
    }

    private BillDetailResponse toResponse(BillDetail billDetail, String imagePath) {
        BillDetailResponse response = new BillDetailResponse();
        response.setId(billDetail.getId());
        response.setQuantity(billDetail.getQuantity());
        response.setPrice(billDetail.getPrice());
        response.setPromotionalPrice(billDetail.getPromotionalPrice());
        response.setStatus(billDetail.getStatus());
        response.setBill(billDetail.getBill());
        response.setProductDetail(billDetail.getProductDetail());
        response.setDefectiveProduct(billDetail.getDefectiveProduct());
        response.setImagePath(imagePath);
        return response;
    }
}
