package com.example.demo.service.thuong;

import com.example.demo.entity.Size;
import com.example.demo.model.request.thuong.SizeRequestTH;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SizeServiceTH {
    Page<Size> getSizes(int page, int size, String name, String sortField, String sortDirection);
    List<Size> findAllByStatus(Integer status);
    Size findById(Long id);
    Size create(SizeRequestTH request);
    Size update(SizeRequestTH request, Long id);
    Size updateStatus(Long id);
    Size delete(Long id);
}
