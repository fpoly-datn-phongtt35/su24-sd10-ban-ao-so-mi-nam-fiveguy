package com.example.demo.service.thuong;

import com.example.demo.entity.Collar;
import com.example.demo.model.request.thuong.CollarRequestTH;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CollarServiceTH {
    Page<Collar> getCollars(int page, int size, String name, String sortField, String sortDirection, Integer status);
    List<Collar> findAllByStatus(Integer status);
    Collar findById(Long id);
    Collar create(CollarRequestTH request);
    Collar update(CollarRequestTH request, Long id);
    Collar updateStatus(Long id);
    Collar delete(Long id);
}
