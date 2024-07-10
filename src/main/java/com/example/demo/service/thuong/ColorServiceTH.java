package com.example.demo.service.thuong;

import com.example.demo.entity.Color;
import com.example.demo.model.request.thuong.ColorRequestTH;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ColorServiceTH {
    Page<Color> getColor(int page, int size, String keyword, String sortField, String sortDirection);
    List<Color> findAllByStatus(Integer status);

    Color findById(Long id);
    Color create(ColorRequestTH request);
    Color update(ColorRequestTH request, Long id);
    Color updateStatus(Long id);
    Color delete(Long id);
}
