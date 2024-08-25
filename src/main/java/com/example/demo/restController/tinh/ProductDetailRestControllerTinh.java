package com.example.demo.restController.tinh;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Image;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import com.example.demo.model.response.tinh.ProductResponseT;
import com.example.demo.repository.tinh.ImageRepositoryTinh;
import com.example.demo.repository.tinh.ProductRepositoryTinh;
import com.example.demo.repository.tinh.SizeRepositoryTinh;
import com.example.demo.service.tinh.ProductDetailServiceTinh;
import com.example.demo.untility.tinh.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/product-tinh")
public class ProductDetailRestControllerTinh {
    @Autowired
    ProductDetailServiceTinh productServiceTinh;

    @Autowired
    ProductRepositoryTinh productRepositoryTinh;

    @Autowired
    ImageRepositoryTinh imageRepositoryTinh;






    @Autowired
    private SizeRepositoryTinh sizeRepository;

    @GetMapping("")
    public ResponseEntity<List<ProductDetail>> getAll(){
        List<ProductDetail> products= productServiceTinh.getAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/page")
    public PaginationResponse<ProductDetail> getEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(defaultValue = "5") int size, // Default value to 5 if not provided
            @RequestParam(required = true, defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, size, sort);
        Page<ProductDetail> page = productServiceTinh.findProductDetal(name, code, price, pageable);
        return new PaginationResponse<>(page);
    }

    @GetMapping("/page-product")
    public PaginationResponse<ProductResponseT> getProduct(
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) Integer totalQuantity) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(pageNumber, size, sort);

//        Page<Product> page = productRepositoryTinh.findAll(pageable);
        List<Product> products = productRepositoryTinh.findAll();
        // Lấy tất cả chi tiết sản phẩm
        List<ProductDetail> productDetails = productServiceTinh.findAllByProductIdIn(
                products.stream().map(Product::getId).collect(Collectors.toList())
        );

        // Lấy tất cả ảnh sản phẩm
        Map<Long, String> productImageMap = imageRepositoryTinh.findByProductIdIn(
                products.stream().map(Product::getId).collect(Collectors.toList())
        ).stream()
                .collect(Collectors.toMap(
                        img -> img.getProduct().getId(),
                        Image::getPath,
                        (existing, replacement) -> existing // Chọn ảnh đầu tiên nếu có nhiều ảnh
                ));

        // Lấy tất cả kích cỡ sản phẩm
        Map<Long, List<String>> productSizeMap = productDetails.stream()
                .collect(Collectors.groupingBy(
                        pd -> pd.getProduct().getId(),
                        Collectors.mapping(pd -> pd.getSize().getName(), Collectors.toList())
                ));

        // Tính tổng số lượng cho mỗi sản phẩm
        Map<Long, Integer> totalQuantityMap = productDetails.stream()
                .collect(Collectors.groupingBy(
                        pd -> pd.getProduct().getId(),
                        Collectors.summingInt(ProductDetail::getQuantity)
                ));

        // Tạo danh sách kết quả với thông tin chi tiết
        List<ProductResponseT> productResponses = products.stream()
                .map(product -> {
                    ProductResponseT response = new ProductResponseT();
                    response.setImage(productImageMap.getOrDefault(product.getId(), null));
                    response.setName(product.getName());
                    response.setTotalQuantity(totalQuantityMap.getOrDefault(product.getId(), 0));
                    response.setPrice(product.getPrice());
                    response.setSizes(productSizeMap.getOrDefault(product.getId(), Collections.emptyList()));
                    return response;
                })
                .filter(response -> totalQuantity == null || response.getTotalQuantity() <= totalQuantity)
                .sorted((r1, r2) -> Integer.compare(r2.getTotalQuantity(), r1.getTotalQuantity()))
                .collect(Collectors.toList());

        // Phân trang kết quả
        int start = Math.min((int) pageable.getOffset(), productResponses.size());
        int end = Math.min(start + pageable.getPageSize(), productResponses.size());
        List<ProductResponseT> pagedProductResponses = productResponses.subList(start, end);

        return new PaginationResponse<>(new PageImpl<>(pagedProductResponses, pageable, productResponses.size()));
    }






    @PutMapping("/update-quantity/{id}")
    public ResponseEntity<ProductDetail> update(@PathVariable Long id, @RequestBody ProductDetail employees) {
        productServiceTinh.update(id, employees);
        if (employees != null) {
            return ResponseEntity.ok(employees);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
