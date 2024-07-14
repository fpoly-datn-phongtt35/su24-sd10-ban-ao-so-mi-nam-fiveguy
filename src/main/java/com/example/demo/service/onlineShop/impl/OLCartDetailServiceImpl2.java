package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.CartDetail;
import com.example.demo.repository.onlineShop.OLCartDetailRepository2;
import com.example.demo.service.onlineShop.OLCartDetailService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service

public class OLCartDetailServiceImpl2 implements OLCartDetailService2 {

    @Autowired
    private OLCartDetailRepository2 olCartDetailRepository;


    @Override
    public List<CartDetail> findAllByCart_Id(Long Id) {
        return olCartDetailRepository.findAllByCart_IdAndStatus(Id,1);
    }

    @Override
    public CartDetail save(CartDetail cartDetail) {
        return olCartDetailRepository.save(cartDetail);
    }




    @Override
    public Optional<CartDetail> findById(Long Id) {
        Optional<CartDetail> cartDetail = olCartDetailRepository.findById(Id);

        if (cartDetail.isPresent()){
            return cartDetail;
        }

        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        olCartDetailRepository.deleteById(id);
    }

    @Override
    public void deleteAllByCart_Id(Long idGioHang) {
        olCartDetailRepository.deleteAllByCart_Id(idGioHang);
    }

    @Override
    public int getTotalQuantityInCart(Long cartId, Long productDetailId) {
        return olCartDetailRepository.getTotalQuantityInCart(cartId, productDetailId);
    }

    @Override
    public CartDetail findCartDetail(Long cartId, Long productDetailId) {
        return olCartDetailRepository.findCartDetail(cartId, productDetailId);
    }

}
