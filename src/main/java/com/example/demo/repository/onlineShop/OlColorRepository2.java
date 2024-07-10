package com.example.demo.repository.onlineShop;

import com.example.demo.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OlColorRepository2 extends JpaRepository<Color, Long> {

    @Query("SELECT DISTINCT c.id, c.name, c.colorCode FROM ProductDetail pd JOIN pd.color c WHERE pd.product.id = :idProduct")
    List<Object[]> getColorsByProductId(@Param("idProduct") Long idProduct);





}