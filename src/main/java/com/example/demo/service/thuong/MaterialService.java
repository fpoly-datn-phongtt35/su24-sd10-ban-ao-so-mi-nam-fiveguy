package com.example.demo.service.thuong;

import com.example.demo.entity.Material;
import com.example.demo.model.request.thuong.MaterialRequest;
import org.springframework.data.domain.Page;

public interface MaterialService {
    Page<Material> getMaterials(int page, int size, String name, String sortField, String sortDirection);
    Material findById(Long id);
    Material create(MaterialRequest request);
    Material update(MaterialRequest request, Long id);
    Material updateStatus(Long id);
    Material delete(Long id);
}
