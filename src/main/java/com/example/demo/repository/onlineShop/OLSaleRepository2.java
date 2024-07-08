package com.example.demo.repository.onlineShop;

import com.example.demo.entity.Sale;
import com.example.demo.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OLSaleRepository2 extends JpaRepository<Sale, Long> {


    @Query("SELECT s.path FROM Sale s WHERE s.status = 1 OR s.status = 2")
    List<String> findAllPathsByStatus();

}
