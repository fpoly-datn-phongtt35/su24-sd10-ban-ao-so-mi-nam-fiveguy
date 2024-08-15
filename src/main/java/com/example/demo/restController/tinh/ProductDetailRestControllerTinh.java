package com.example.demo.restController.tinh;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Image;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import com.example.demo.repository.tinh.ImageRepositoryTinh;
import com.example.demo.repository.tinh.ProductRepositoryTinh;
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
    public PaginationResponse<Map<String, Object>> getProduct(
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) Integer totalQuantity) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, size, sort);

        // Truy xuất tất cả sản phẩm để tính tổng số lượng và ảnh
        List<Object[]> allResults = productRepositoryTinh.findAllProductAndDetails();
        Map<Long, Integer> totalQuantityMap = new HashMap<>();
        Map<Long, List<ProductDetail>> productDetailMap = new HashMap<>();
        Map<Long, List<String>> productImageMap = new HashMap<>(); // Lưu thông tin ảnh

        for (Object[] result : allResults) {
            Product product = (Product) result[0];
            ProductDetail productDetail = (ProductDetail) result[1];

            // Cập nhật tổng số lượng và chi tiết sản phẩm
            totalQuantityMap.put(product.getId(), totalQuantityMap.getOrDefault(product.getId(), 0) + productDetail.getQuantity());
            productDetailMap.computeIfAbsent(product.getId(), k -> new ArrayList<>()).add(productDetail);

            // Lấy danh sách ảnh từ bảng ProductImage
            List<Image> images = imageRepositoryTinh.findByProductId(product.getId());
            List<String> imageUrls = images.stream()
                    .map(Image::getPath) // Giả sử có phương thức getImageUrl() trong ProductImage
                    .collect(Collectors.toList());
            productImageMap.put(product.getId(), imageUrls); // Lưu ảnh theo ID sản phẩm
        }

        // Truy xuất các sản phẩm phân trang
        Page<Product> page = productRepositoryTinh.findDistinctProducts(pageable);

        // Kết hợp thông tin từ cả hai bước trên, lọc và sắp xếp theo totalQuantity
        List<Map<String, Object>> productAndDetails = page.getContent().stream()
                .map(product -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("product", product);
                    map.put("productDetails", productDetailMap.get(product.getId()));
                    map.put("totalQuantity", totalQuantityMap.getOrDefault(product.getId(), 0)); // Sử dụng giá trị mặc định là 0
                    map.put("images", productImageMap.get(product.getId())); // Thêm ảnh vào map
                    return map;
                })
                .filter(map -> totalQuantity == null || (Integer) map.get("totalQuantity") <= totalQuantity)
                .sorted((m1, m2) -> ((Integer) m2.get("totalQuantity")).compareTo((Integer) m1.get("totalQuantity")))
                .collect(Collectors.toList());

        // Áp dụng phân trang cho danh sách đã sắp xếp và lọc
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productAndDetails.size());
        if (start > end) {
            start = end; // Điều chỉnh start để tránh lỗi
        }
        List<Map<String, Object>> pagedProductAndDetails = productAndDetails.subList(start, end);

        Page<Map<String, Object>> resultPage = new PageImpl<>(pagedProductAndDetails, pageable, productAndDetails.size());

        return new PaginationResponse<>(resultPage);
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
