package com.example.demo.service.thuong;

import com.example.demo.entity.Wrist;
import com.example.demo.model.request.thuong.WristRequestTH;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WristServiceTH {
    Page<Wrist> getWrists(int page, int size, String name, String sortField, String sortDirection, Integer status);
    List<Wrist> findAllByStatus(Integer status);
    Wrist findById(Long id);
    Wrist create(WristRequestTH request);
    Wrist update(WristRequestTH request, Long id);
    Wrist updateStatus(Long id);
    Wrist delete(Long id);
}
