package com.example.demo.restController.onlineShop;

import com.example.demo.entity.*;
import com.example.demo.model.response.onlineShop.CartResponseDTO;
import com.example.demo.security.service.SCCustomerService;
import com.example.demo.security.service.SCEmployeeService;
import com.example.demo.service.onlineShop.*;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/home")
public class OLCartController2 {

    @Autowired
    private SCEmployeeService SCEmployeeService;

    @Autowired
    private SCCustomerService SCCustomerService;

    @Autowired
    private OLCartService2 olCartService;

    @Autowired
    private OLCartDetailService2 olCartDetailService;

    @Autowired
    private OLProductDetailService2 olProductDetailService;

    @Autowired
    private OLProductService2 olProductService2;

    @Autowired
    private OLImageService2 olImageService2;


    @GetMapping("/cart/cartDetail")
    public ResponseEntity<List<CartResponseDTO>> getCart(@RequestHeader("Authorization") String token) {
        Optional<Customer> customer = SCCustomerService.getCustomerByToken(token);

            if (customer.isPresent()) {
                Cart gioHang = olCartService.findByCustomerId(customer.get().getId());
                if (gioHang != null) {
                    List<CartDetail> chiTietGioHang = olCartDetailService.findAllByCart_Id(gioHang.getId());
                    List<CartResponseDTO> olCartResponses = new ArrayList<>();
                    for (CartDetail cartDetail : chiTietGioHang) {
                        Product product = cartDetail.getProductDetail().getProduct();

                        CartResponseDTO olCartResponse = new CartResponseDTO();
                        olCartResponse.setId(cartDetail.getId());
                        olCartResponse.setQuantity(cartDetail.getQuantity());
//                        phh
                        olCartResponse.setPrice(olProductService2.getProductPriceById(product.getId()));
                        olCartResponse.setPromotionalPrice(olProductService2.findPromotionalPriceByProductId(product.getId()));
                        olCartResponse.setCart(cartDetail.getCart());
                        olCartResponse.setName(product.getName() +  product.getCategory().getName() + product.getMaterial().getName());
                        olCartResponse.setNameColor(cartDetail.getProductDetail().getColor().getName());
                        olCartResponse.setNameSize(cartDetail.getProductDetail().getSize().getName());
                        olCartResponse.setProductDetail(cartDetail.getProductDetail());
                        List<String> images = olImageService2.getImagesByProductIdAndColorId(product.getId(), cartDetail.getProductDetail().getColor().getId());
                        String imagePath = null;

                        for (String image : images) {
                            if (image != null && !image.isEmpty()) {
                                imagePath = image;
                                break;
                            }
                        }
                        if (imagePath != null) {
                            olCartResponse.setPath(imagePath);
                        }

                        olCartResponses.add(olCartResponse);
                    }
                    return ResponseEntity.ok(olCartResponses);
                }
            }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PostMapping("/cart/add")
    public ResponseEntity<?> creat(@RequestHeader("Authorization") String token, @RequestBody JsonNode orderData) {

        Optional<Employee> employee = SCEmployeeService.getEmployeeByToken(token);
        Optional<Customer> customer = SCCustomerService.getCustomerByToken(token);

        if (employee.isPresent()) {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("employeeLoggedIn", true);
            return ResponseEntity.ok(responseData);
        }

        if (customer.isPresent()) {
            Cart cart = olCartService.findByCustomerId(customer.get().getId());

            if (cart == null) {
                cart = new Cart();
                cart.setCustomer(customer.get());
                cart.setStatus(1);
                cart = olCartService.save(cart);
            }

            Long productDetailId = Long.valueOf(orderData.get("productDetailId").asText());
            int quantity = orderData.get("quantity").asInt();
//            String promotionalPriceString = orderData.get("promotionalPrice").asText();

//            BigDecimal promotionalPrice = new BigDecimal(promotionalPriceString);

            Optional<ProductDetail> productDetail = olProductDetailService.findById(productDetailId);

            if (productDetail.isPresent()) {
                int availableQuantity = productDetail.get().getQuantity();
                int totalQuantityInCart = olCartDetailService.getTotalQuantityInCart(cart.getId(), productDetailId);
                int remainingQuantity = availableQuantity - totalQuantityInCart;

                if (remainingQuantity >= quantity) {
                    CartDetail cartDetail = olCartDetailService.findCartDetail(cart.getId(), productDetailId);

                    if (cartDetail != null) {
                        cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
                        olCartDetailService.save(cartDetail);
                    } else {
                        CartDetail newCartDetail = new CartDetail();
                        newCartDetail.setCart(cart);
                        newCartDetail.setProductDetail(productDetail.get());
                        newCartDetail.setQuantity(quantity);
//                        newCartDetail.setPrice(productDetail.get().getProduct().getPrice());
//                        newCartDetail.setPromotionalPrice(promotionalPrice);
                        newCartDetail.setStatus(1);
                        olCartDetailService.save(newCartDetail);
                    }
                    return ResponseEntity.ok(1); // Thêm thành công
                } else {
                    return ResponseEntity.ok(2); // Số lượng không đủ
                }
            } else {
                return ResponseEntity.ok(0); // Product not found
            }
        } else {
            return ResponseEntity.ok(0); // Customer not found
        }
    }

    @PostMapping("/cart/update")
    public ResponseEntity<?> update(@RequestBody JsonNode orderData) {
        String cartDetailId = orderData.get("cartDetailId").asText();
        int quantity = orderData.get("quantity").asInt();

        if (cartDetailId != null && quantity >= 0) {
            Optional<CartDetail> cartDetail = olCartDetailService.findById(Long.valueOf(cartDetailId));
            if (cartDetail.isPresent()) {
                Optional<ProductDetail> productDetail = olProductDetailService.findById(cartDetail.get().getProductDetail().getId());
                int availableQuantity = productDetail.get().getQuantity();

                int totalQuantityInCart = olCartDetailService.getTotalQuantityInCart(cartDetail.get().getCart().getId(), cartDetail.get().getProductDetail().getId());
                int remainingQuantity = availableQuantity - (totalQuantityInCart - cartDetail.get().getQuantity());

                if (remainingQuantity >= quantity) {
                    // Số lượng đủ, tiến hành cập nhật
                    int newQuantity = quantity;
                    if (quantity == 0) {
                        // Nếu quantity là 0, xóa CartDetail tương ứng
                        olCartDetailService.deleteById(cartDetail.get().getId());
                        newQuantity = cartDetail.get().getQuantity();
                    } else {
                        cartDetail.get().setQuantity(quantity);
                        olCartDetailService.save(cartDetail.get());
                    }

                    int updatedQuantity = productDetail.get().getQuantity() + (cartDetail.get().getQuantity() - newQuantity);
                    productDetail.get().setQuantity(updatedQuantity);
                    olProductDetailService.save(productDetail.get());

                    return ResponseEntity.ok(1);
                } else {
                    // Số lượng không đủ
                    return ResponseEntity.ok(2);
                }
            }
        }
        return ResponseEntity.ok(0);

    }

    @Transactional
    @PostMapping("/cart/remove")
    public void remove(@RequestBody JsonNode orderData) {
        String cartDetailId = orderData.get("cartDetailId").asText(); // Lấy giá trị UUID dưới dạng String
        if (cartDetailId != null) {
            Optional<CartDetail> chiTietGioHang = olCartDetailService.findById(Long.valueOf(cartDetailId));
            if (chiTietGioHang.isPresent()) {
                olCartDetailService.deleteById(Long.valueOf(cartDetailId));
            }
        }
    }
    @Transactional
    @PostMapping("/cart/clear")
    public void clearCart(@RequestHeader("Authorization") String token) {

        Optional<Customer> customer = SCCustomerService.getCustomerByToken(token);
            if (customer.isPresent()) {
                Cart gioHang = olCartService.findByCustomerId(customer.get().getId());
                if (gioHang != null) {
                    olCartDetailService.deleteAllByCart_Id(gioHang.getId());

                }
            }
    }

}
