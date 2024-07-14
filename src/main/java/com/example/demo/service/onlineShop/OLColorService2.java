package com.example.demo.service.onlineShop;

import com.example.demo.entity.Color;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OLColorService2 {


    List<Object[]> getColorsByProductId( Long idProduct);
}
