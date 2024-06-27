package com.example.demo.service.thuong;

import com.example.demo.entity.Wrist;
import com.example.demo.model.request.thuong.WristRequest;
import org.springframework.data.domain.Page;

public interface WristService {
    Page<Wrist> getWrists(int page, int size, String name, String sortField, String sortDirection);
    Wrist findById(Long id);
    Wrist create(WristRequest request);
    Wrist update(WristRequest request, Long id);
    Wrist updateStatus(Long id);
    Wrist delete(Long id);
}
