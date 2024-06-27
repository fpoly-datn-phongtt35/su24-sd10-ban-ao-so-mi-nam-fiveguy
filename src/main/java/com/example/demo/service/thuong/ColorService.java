package com.example.demo.service.thuong;

import com.example.demo.entity.Color;
import com.example.demo.model.request.thuong.ColorRequest;
import org.springframework.data.domain.Page;

public interface ColorService {
    Page<Color> getColor(int page, int size, String keyword, String sortField, String sortDirection);
    Color findById(Long id);
    Color create(ColorRequest request);
    Color update(ColorRequest request, Long id);
    Color updateStatus(Long id);
    Color delete(Long id);
}
