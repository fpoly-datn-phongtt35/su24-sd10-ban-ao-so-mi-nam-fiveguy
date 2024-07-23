package com.example.demo.restController.nguyen;

import com.example.demo.entity.ProductDetail;
import com.example.demo.model.response.nguyen.ProductDetailResponse;
import com.example.demo.service.nguyen.NProductDetailService;
import com.example.demo.service.nguyen.NProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/productDetail")
public class ProductDetailRestController {

    @Autowired
    private NProductDetailService productDetailService;

    @Autowired
    private NProductService productService;

    @GetMapping("/page")
    public ResponseEntity<?> searchProductDetails(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long materialId,
            @RequestParam(required = false) Long wristId,
            @RequestParam(required = false) Long collarId,
            @RequestParam(required = false) Long sizeId,
            @RequestParam(required = false) Long colorId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDetail> productDetails = productDetailService
                .searchProductDetails(productName, categoryId, materialId, wristId, collarId,
                        sizeId, colorId, minPrice == null ? null : BigDecimal.valueOf(minPrice),
                        maxPrice == null ? null : BigDecimal.valueOf(maxPrice), pageable);

        Page<ProductDetailResponse> responsePage = productDetails.map(productDetail -> {
            String imagePath = productService.getImagePathByProductId(productDetail.getProduct().getId());
            return toResponse(productDetail, imagePath);
        });

        return ResponseEntity.ok(responsePage);
    }

    private ProductDetailResponse toResponse(ProductDetail productDetail, String imagePath) {
        ProductDetailResponse response = new ProductDetailResponse();
        response.setId(productDetail.getId());
        response.setQuantity(productDetail.getQuantity());
        response.setBarcode(productDetail.getBarcode());
        response.setCreatedAt(productDetail.getCreatedAt());
        response.setUpdatedAt(productDetail.getUpdatedAt());
        response.setCreatedBy(productDetail.getCreatedBy());
        response.setUpdatedBy(productDetail.getUpdatedBy());
        response.setStatus(productDetail.getStatus());
        response.setProduct(productDetail.getProduct());
        response.setSize(productDetail.getSize());
        response.setColor(productDetail.getColor());
        response.setImagePath(imagePath);
        return response;
    }

}
