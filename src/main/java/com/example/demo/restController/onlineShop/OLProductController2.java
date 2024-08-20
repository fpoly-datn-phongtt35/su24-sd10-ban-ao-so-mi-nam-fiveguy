package com.example.demo.restController.onlineShop;

import com.example.demo.entity.Category;
import com.example.demo.entity.Image;
import com.example.demo.entity.ProductDetail;
import com.example.demo.model.response.onlineShop.ProductDetailsDTO;
import com.example.demo.model.response.onlineShop.ProductSaleDetails;
import com.example.demo.service.onlineShop.OLCategoryService2;
import com.example.demo.service.onlineShop.OLImageService2;
import com.example.demo.service.onlineShop.OLProductDetailService2;
import com.example.demo.service.onlineShop.OLProductService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/home")
public class OLProductController2 {

    @Autowired
    private OLProductService2 productService;

    @Autowired
    private OLProductDetailService2 olProductDetailService2;

    @Autowired
    private OLImageService2 olImageService2;



//    @GetMapping("/product/filter")
//    public Page<ProductSaleDetails> filterProducts(
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) String colorIdParam,
//            @RequestParam(required = false) String sizeIdParam,
//            @RequestParam(required = false) String materialIdParam,
//            @RequestParam(required = false) String collarIdParam,
//            @RequestParam(required = false) String wristIdParam,
//            @RequestParam(required = false) String categoryIdParam,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "desc") String sortDir,
//            @RequestParam(defaultValue = "createdAt") String sort) {
//
//        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
//
//        Long colorId = parseId(colorIdParam);
//        Long sizeId = parseId(sizeIdParam);
//        Long materialId = parseId(materialIdParam);
//        Long collarId = parseId(collarIdParam);
//        Long wristId = parseId(wristIdParam);
//        Long categoryId = parseId(categoryIdParam);
//
//        return productService.filterProducts(
//                categoryId, name, colorId, sizeId, materialId, collarId, wristId, pageable);
//    }
//
//    private Long parseId(String idString) {
//        if (idString != null && !idString.isEmpty()) {
//            try {
//                return Long.parseLong(idString);
//            } catch (NumberFormatException e) {
//                // Handle parsing error if needed
//                return null;
//            }
//        }
//        return null;
//    }



    @GetMapping("/products/filter")
    public Page<ProductSaleDetails> filterProducts(
            @RequestParam(required = false) Set<String> categoryIds,
            @RequestParam(required = false) Set<String> collarIds,
            @RequestParam(required = false) Set<String> wristIds,
            @RequestParam(required = false) Set<String> colorIds,
            @RequestParam(required = false) Set<String> sizeIds,
            @RequestParam(required = false) Set<String> materialIds,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(defaultValue = "createdAt") String sort) {

        // Chuyển đổi Set<String> sang Set<Long>
        Set<Long> categoryIdSet = convertToLongSet(categoryIds);
        Set<Long> collarIdSet = convertToLongSet(collarIds);
        Set<Long> wristIdSet = convertToLongSet(wristIds);
        Set<Long> colorIdSet = convertToLongSet(colorIds);
        Set<Long> sizeIdSet = convertToLongSet(sizeIds);
        Set<Long> materialIdSet = convertToLongSet(materialIds);

        // Xác định hướng sắp xếp và thuộc tính sắp xếp
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortBy;
        if ("price".equals(sort)) {
            sortBy = "promotionalPrice";
        } else {
            sortBy = "createdAt";
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return productService.filterProducts2(
                categoryIdSet, collarIdSet, wristIdSet, colorIdSet, sizeIdSet, materialIdSet, searchTerm, pageable);
    }

    private Set<Long> convertToLongSet(Set<String> stringSet) {
        Set<Long> longSet = new HashSet<>();
        if (stringSet != null) {
            for (String s : stringSet) {
                try {
                    longSet.add(Long.parseLong(s));
                } catch (NumberFormatException e) {
                    // Xử lý lỗi chuyển đổi nếu cần thiết
                }
            }
        }
        return longSet;
    }

    @GetMapping("/product/viewProduct/{id}")
    public ProductDetailsDTO getProductDetails(@PathVariable("id") Long id) {
        return productService.getProductDetails(id);
    }

    @GetMapping("/product/{productId}/color/{colorId}")
    public List<String> getImagesByProductIdAndColorId(@PathVariable Long productId, @PathVariable Long colorId) {
        return olImageService2.getImagesByProductIdAndColorId(productId, colorId);
    }

    @GetMapping("/product/productDetail/{productId}/{sizeId}/{colorId}")
    public ResponseEntity<ProductDetail> getProductDetail(@PathVariable Long productId,
                                                          @PathVariable Long sizeId,
                                                          @PathVariable Long colorId) {
        ProductDetail productDetail = olProductDetailService2.getProductDetail(productId, sizeId, colorId);
        if (productDetail != null) {
            return ResponseEntity.ok(productDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/product/totalQuantitySold")
    public List<ProductSaleDetails> getAllProductsOrderedByTotalQuantitySold() {
        return productService.findAllProductsOrderedByTotalQuantitySold();
    }

    @GetMapping("/product/createdAt")
    public List<ProductSaleDetails> getProductsOrderedByCreatedAt() {
        return productService.findProductsOrderedByCreatedAt();
    }

    @GetMapping("/product/search")
    public List<ProductSaleDetails> searchProducts(@RequestParam("name") String name) {
        return productService.search(name);
    }

    @GetMapping("/products/category")
    public ResponseEntity<?> getProductByCategory(@RequestParam(value = "productId", required = false) Long productId) {
        List<ProductSaleDetails> searchResults = productService.findProductsByCategoryId(productId);
        return ResponseEntity.ok(searchResults);
    }


}