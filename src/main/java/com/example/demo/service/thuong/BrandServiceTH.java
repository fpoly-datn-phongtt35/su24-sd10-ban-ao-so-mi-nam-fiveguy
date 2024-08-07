package com.example.demo.service.thuong;

import com.example.demo.entity.Brand;
import com.example.demo.model.request.thuong.BrandRequestTH;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BrandServiceTH {
    Page<Brand> getBrands(int page, int size, String name, String sortField, String sortDirection, Integer status);
    List<Brand> findAllByStatus(Integer status);
    Brand findById(Long id);
    Brand create(BrandRequestTH request);
    Brand update(BrandRequestTH request, Long id);
    Brand updateStatus(Long id);
    Brand delete(Long id);
}
