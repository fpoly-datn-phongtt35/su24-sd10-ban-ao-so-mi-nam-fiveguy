package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.advice.DuplicateException;
import com.example.demo.entity.Brand;
import com.example.demo.entity.BrandSuppiler;
import com.example.demo.entity.Supplier;
import com.example.demo.model.request.thuong.SupplierRequestTH;
import com.example.demo.model.request.thuong.SupplierUpdateRequestTH;
import com.example.demo.repository.thuong.BrandRepositoryTH;
import com.example.demo.repository.thuong.SupplierRepositoryTH;
import com.example.demo.service.thuong.SupplierServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SupplierServiceTHImpl implements SupplierServiceTH {
    @Autowired
    private SupplierRepositoryTH repository;

    @Autowired
    private BrandRepositoryTH brandRepositoryTH;

    @Override
    public Page<Supplier> getSuppliers(int page, int size, String keyword, String sortField, String sortDirection, Integer status) {
        Sort sort = Sort.by(sortField);
        if ("DESC".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        if (keyword == null || keyword.isEmpty())
            return repository.findAllAndStatus(status, pageable);
        else return repository.findByKeyword(keyword, status, pageable);
    }

    @Override
    public List<Supplier> findAllByStatus(Integer status) {
        return repository.findAllByStatus(status);
    }

    @Override
    public Supplier findById(Long id) {
        return repository.findById(id).orElse(null);    }

    @Override
    public Supplier create(SupplierRequestTH request) {
        Supplier existingSupplier= repository.findByName(request.getName());
        if (existingSupplier != null) {
            throw new DuplicateException("Trùng tên nhà cung cấp", "name");
        }
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setAddress(request.getAddress());
        supplier.setPhoneNumber(request.getPhoneNumber());
        supplier.setEmail(request.getEmail());
        supplier.setStatus(1);
        supplier.setCreatedAt(new Date());
        Set<BrandSuppiler> brandSuppilers = request.getBrands().stream().map(brand -> {
            Brand existingBrand = brandRepositoryTH.findById(brand.getId()).orElse(null);
            BrandSuppiler brandSuppiler = new BrandSuppiler();
            brandSuppiler.setBrand(existingBrand);
            brandSuppiler.setSupplier(supplier);
            brandSuppiler.setStatus(1);
            return brandSuppiler;
        }).collect(Collectors.toSet());
        supplier.setBrandSuppilers(brandSuppilers);

        return repository.save(supplier);
    }

    @Override
    public Supplier update(SupplierUpdateRequestTH request, Long id) {
        Supplier existingSupplier = repository.findByName(request.getName());
        if (existingSupplier != null && !existingSupplier.getId().equals(id)) {
            throw new DuplicateException("Trùng tên nhà cung cấp", "name");
        }
        Optional<Supplier> supplierOptional = repository.findById(id);
        if (supplierOptional.isPresent()) {
            Supplier supplier = supplierOptional.get();
            supplier.setName(request.getName());
            supplier.setAddress(request.getAddress());
            supplier.setPhoneNumber(request.getPhoneNumber());
            supplier.setEmail(request.getEmail());
            supplier.setUpdatedAt(new Date());
            supplier.setBrandSuppilers(request.getBrandSuppilers());
            return repository.save(supplier);
        }
        return null;
    }

    @Override
    public Supplier updateStatus(Long id) {
        Optional<Supplier> supplierOptional = repository.findById(id);
        if (supplierOptional.isPresent()) {
            Supplier supplier = supplierOptional.get();
            supplier.setUpdatedAt(new Date());
            supplier.setStatus(1);
            return repository.save(supplier);
        }
        return null;
    }

    @Override
    public Supplier delete(Long id) {
        Optional<Supplier> supplierOptional = repository.findById(id);
        if (supplierOptional.isPresent()) {
            Supplier size = supplierOptional.get();
            size.setStatus(0);
            return repository.save(size);
        }
        return null;
    }
}
