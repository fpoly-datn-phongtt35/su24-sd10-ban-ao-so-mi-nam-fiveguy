package com.example.demo.model.request.nguyen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BillDetailRequest {

    Long productDetailId;

    int quantity;

    BigDecimal price;

    BigDecimal promotionalPrice;
}
