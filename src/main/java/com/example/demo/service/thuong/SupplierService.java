package com.example.demo.service.thuong;

import com.example.demo.entity.Supplier;
import com.example.demo.model.request.thuong.SupplierRequest;
import org.springframework.data.domain.Page;

public interface SupplierService {
    Page<Supplier> getSuppliers(int page, int size, String name, String sortField, String sortDirection);
    Supplier findById(Long id);
    Supplier create(SupplierRequest request);
    Supplier update(SupplierRequest request, Long id);
    Supplier updateStatus(Long id);
    Supplier delete(Long id);
}
