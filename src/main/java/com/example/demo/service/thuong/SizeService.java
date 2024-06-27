package com.example.demo.service.thuong;

import com.example.demo.entity.Size;
import com.example.demo.model.request.thuong.SizeRequest;
import org.springframework.data.domain.Page;

public interface SizeService {
    Page<Size> getSizes(int page, int size, String name, String sortField, String sortDirection);
    Size findById(Long id);
    Size create(SizeRequest request);
    Size update(SizeRequest request, Long id);
    Size updateStatus(Long id);
    Size delete(Long id);
}
