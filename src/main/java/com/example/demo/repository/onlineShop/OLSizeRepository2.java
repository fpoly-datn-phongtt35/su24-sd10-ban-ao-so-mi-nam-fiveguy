package com.example.demo.repository.onlineShop;

import com.example.demo.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OLSizeRepository2 extends JpaRepository<Size, Long> {

    @Query("SELECT DISTINCT s.id, s.name " +
            "FROM ProductDetail pd " +
            "JOIN pd.size s " +
            "WHERE pd.product.id = :idProduct")
    List<Object[]> getSizesByProductId(@Param("idProduct") Long idProduct);





}