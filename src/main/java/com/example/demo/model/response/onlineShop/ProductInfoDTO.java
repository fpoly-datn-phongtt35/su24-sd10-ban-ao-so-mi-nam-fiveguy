package com.example.demo.model.response.onlineShop;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductInfoDTO {
        private Long id;
        private String productName;
        private BigDecimal price;
        private String wristName;
        private String materialName;
        private String categoryName;
        private String collarName;
        private Integer promotionalPrice;
        private Integer discountType;
        private Integer value;
        private Integer totalQuantitySold;
        private String brandName;
        private String describe;

        public ProductInfoDTO(Long id, String productName, BigDecimal price, String wristName, String materialName, String categoryName, String collarName, Integer promotionalPrice, Integer discountType, Integer value, Integer totalQuantitySold, String brandName, String describe) {
                this.id = id;
                this.productName = productName;
                this.price = price;
                this.wristName = wristName;
                this.materialName = materialName;
                this.categoryName = categoryName;
                this.collarName = collarName;
                this.promotionalPrice = promotionalPrice;
                this.discountType = discountType;
                this.value = value;
                this.totalQuantitySold = totalQuantitySold;
                this.brandName = brandName;
                this.describe = describe;
        }
}
