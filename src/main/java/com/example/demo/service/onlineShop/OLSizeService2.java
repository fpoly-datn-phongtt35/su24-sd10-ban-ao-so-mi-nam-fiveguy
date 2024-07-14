package com.example.demo.service.onlineShop;

import com.example.demo.entity.Size;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OLSizeService2 {


    List<Object[]> getSizesByProductId( Long idProduct);

}
