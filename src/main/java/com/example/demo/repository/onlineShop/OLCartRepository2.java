package com.example.demo.repository.onlineShop;

import com.example.demo.entity.Cart;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OLCartRepository2 extends JpaRepository<Cart, Long> {

    Cart findByCustomerId(Long id);



}
