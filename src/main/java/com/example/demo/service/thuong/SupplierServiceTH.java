package com.example.demo.service.thuong;

import com.example.demo.entity.Supplier;
import com.example.demo.model.request.thuong.SupplierRequestTH;
import com.example.demo.model.request.thuong.SupplierUpdateRequestTH;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SupplierServiceTH {
    Page<Supplier> getSuppliers(int page, int size, String name, String sortField, String sortDirection);
    List<Supplier> findAllByStatus(Integer status);
    Supplier findById(Long id);
    Supplier create(SupplierRequestTH request);
    Supplier update(SupplierUpdateRequestTH request, Long id);
    Supplier updateStatus(Long id);
    Supplier delete(Long id);
}
