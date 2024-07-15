package com.example.demo.service.Customer.ServiceImpl;

import com.example.demo.entity.Cart;
import com.example.demo.repository.Customer.CartRepositoryH;
import com.example.demo.service.Customer.AccountService;
import com.example.demo.service.Customer.CartService;
import com.example.demo.service.Customer.CustomerService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
//OL

    @Autowired
    private AccountService accountService;

    @Autowired
    private CartRepositoryH olCartRepositoryH;

    @Autowired
    private CustomerService olCustomerService;

//    @Autowired
//    private CartService olCartService;

//    @Autowired
//    private CartDetailService olCartDetailService;

//    @Autowired
//    private ProductDetailService olProductDetailService;

//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


    @Override
    public Cart findByCustomerId(Long id) {
        return olCartRepositoryH.findByCustomerId(id);
    }

    @Override
    public Cart save(Cart gioHang) {
        return olCartRepositoryH.save(gioHang);
    }

    @Override
    public Cart saveAllProductDetail(JsonNode orderData) {
//        String currentUsername =String.valueOf(orderData.get("username").asText());
//        Optional<Account> account = accountService.findByAccountLogin(currentUsername);
//        if (account.isPresent()) {
//            Optional<Customer> customer = Optional.ofNullable(olCustomerService.findByAccount_Id(account.get().getId()));
//
//            if (customer.isPresent()) {
//                Cart cart = olCartRepository.findByCustomerId(customer.get().getId());
//
//                if (cart == null) {
//                    cart = new Cart();
//                    cart.setCustomer(customer.get());
//                    cart.setCreatedAt(new Date());
//                    cart.setUpdatedAt(new Date());
//                    cart.setStatus(1);
//                    cart = olCartRepository.save(cart);
//                }
//
//                List<CartDetail> cartDetails = olCartDetailService.findAllByCart_Id(cart.getId());
//
//                for (JsonNode item : orderData.get("localStorageData")) {
//                    Long productId = Long.valueOf(item.get("id").asText());
//                    Optional<ProductDetail> productDetail = olProductDetailService.findById(productId);
//
//                    if (productDetail.isPresent()) {
//                        Optional<CartDetail> foundCartDetail = cartDetails.stream()
//                                .filter(cd -> cd.getProductDetail().getId().equals(productDetail.get().getId()))
//                                .findFirst();
//
//                        if (foundCartDetail.isPresent()) {
//                            foundCartDetail.get().setQuantity(foundCartDetail.get().getQuantity() + Integer.valueOf(item.get("quantity").asText()));
//                            olCartDetailService.save(foundCartDetail.get());
//                        } else {
//                            CartDetail newCartDetail = new CartDetail();
//                            newCartDetail.setCart(cart);
//                            newCartDetail.setPrice(BigDecimal.valueOf(Long.valueOf(item.get("price").asText())));
//                            newCartDetail.setProductDetail(productDetail.get());
//                            newCartDetail.setQuantity(Integer.valueOf(item.get("quantity").asText()));
//                            newCartDetail.setStatus(1);
//                            olCartDetailService.save(newCartDetail);
//                        }
//                    }
//                }
//                return cart;
//            }
//        }
        return null;
    }
// END OL
}
