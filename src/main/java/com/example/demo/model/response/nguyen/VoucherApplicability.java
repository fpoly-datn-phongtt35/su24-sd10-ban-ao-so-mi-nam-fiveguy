package com.example.demo.model.response.nguyen;

import com.example.demo.entity.Voucher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoucherApplicability {

    private Voucher voucher;
    private boolean isApplicable;
    private long usedCount;
    private String reason;
}
