package com.example.demo.service;

import com.example.demo.entity.Collar;
import com.example.demo.model.request.CollarRequest;
import org.springframework.data.domain.Page;

public interface CollarService {
    Page<Collar> getCollars(int page, int size, String name, String sortField, String sortDirection);
    Collar findById(Long id);
    Collar create(CollarRequest request);
    Collar update(CollarRequest request, Long id);
    Collar updateStatus(Long id);
    Collar delete(Long id);
}
