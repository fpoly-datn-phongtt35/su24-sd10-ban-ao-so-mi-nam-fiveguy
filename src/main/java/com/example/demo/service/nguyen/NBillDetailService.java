package com.example.demo.service.nguyen;

import com.example.demo.entity.BillDetail;
import com.example.demo.model.response.nguyen.BillDetailSummary;

import java.math.BigDecimal;
import java.util.List;

public interface NBillDetailService {

    BillDetail getById(Long id);

    List<BillDetail> getAllByBillId(Long billId);

    BillDetailSummary getBillDetailSummaryByBillId(Long billId);

    BillDetail addProductDetailToBill(Long billId, Long productDetailId, int quantity,
                                      BigDecimal price, BigDecimal promotionalPrice);

    void removeProductDetailFromBill(Long billDetailId);

    BillDetail updateBillDetailQuantity(Long billDetailId, int newQuantity);
}
