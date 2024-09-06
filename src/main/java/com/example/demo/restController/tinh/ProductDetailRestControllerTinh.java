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

    @GetMapping("/page-product")
    public PaginationResponse<ProductResponseT> getProduct(
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false) Integer totalQuantity) {

        // Retrieve products with status = 1
        List<Product> products = productRepositoryTinh.findProductsWithStatusOne();

        // Get all product details based on the retrieved products
        List<ProductDetail> productDetails = productServiceTinh.findAllByProductIdIn(
                products.stream().map(Product::getId).collect(Collectors.toList())
        );

        // Get all sizes for the products
        Map<Long, List<String>> productSizeMap = productDetails.stream()
                .collect(Collectors.groupingBy(
                        pd -> pd.getProduct().getId(),
                        Collectors.mapping(pd -> pd.getSize().getName(), Collectors.toList())
                ));

        // Calculate total quantity for each product
        Map<Long, Integer> totalQuantityMap = productDetails.stream()
                .collect(Collectors.groupingBy(
                        pd -> pd.getProduct().getId(),
                        Collectors.summingInt(ProductDetail::getQuantity)
                ));

        // Create the response list with detailed product info
        List<ProductResponseT> productResponses = products.stream()
                .map(product -> {
                    ProductResponseT response = new ProductResponseT();
                    List<String> imagePaths = imageRepositoryTinh.findImagePathsByProductId(product.getId());
                    response.setImage(imagePaths != null && !imagePaths.isEmpty() ? imagePaths.get(0) : null);
                    response.setName(product.getName());
                    response.setTotalQuantity(totalQuantityMap.getOrDefault(product.getId(), 0));
                    response.setPrice(product.getPrice());
                    response.setSizes(productSizeMap.getOrDefault(product.getId(), Collections.emptyList()));
                    return response;
                })
                .filter(response -> response.getTotalQuantity() > 0) // Filter for total quantity > 0
                .filter(response -> totalQuantity == null || response.getTotalQuantity() <= totalQuantity) // Apply additional filter if needed
                .sorted((r1, r2) -> Integer.compare(r2.getTotalQuantity(), r1.getTotalQuantity())) // Sort by total quantity
                .collect(Collectors.toList());

        // Calculate pagination boundaries
        int start = pageNumber * size;
        int end = Math.min(start + size, productResponses.size());

        // Paginate the results
        List<ProductResponseT> pagedProductResponses = productResponses.subList(start, end);

        // Create PageImpl with correct total size
        Page<ProductResponseT> pageResult = new PageImpl<>(pagedProductResponses, PageRequest.of(pageNumber, size), productResponses.size());

        return new PaginationResponse<>(pageResult);
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
