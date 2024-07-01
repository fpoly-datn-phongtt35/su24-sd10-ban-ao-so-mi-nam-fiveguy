package com.example.demo.service.onlineShop;

import com.example.demo.entity.CartDetail;

import java.util.List;
import java.util.Optional;

public interface OLCartDetailService2 {

    List<CartDetail> findAllByCart_Id(Long uuid);

    CartDetail save(CartDetail gioHang);

    Optional<CartDetail> findById(Long Id);

    void deleteById(Long id);

    void deleteAllByCart_Id(Long idGioHang);

    int getTotalQuantityInCart( Long cartId,  Long productDetailId);

    CartDetail findCartDetail( Long cartId, Long productDetailId);
}
