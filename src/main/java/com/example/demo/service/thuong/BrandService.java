package com.example.demo.service.thuong;

import com.example.demo.entity.Brand;
import com.example.demo.model.request.thuong.BrandRequest;
import org.springframework.data.domain.Page;

public interface BrandService {
    Page<Brand> getBrands(int page, int size, String name, String sortField, String sortDirection);
    Brand findById(Long id);
    Brand create(BrandRequest request);
    Brand update(BrandRequest request, Long id);
    Brand updateStatus(Long id);
    Brand delete(Long id);
}
