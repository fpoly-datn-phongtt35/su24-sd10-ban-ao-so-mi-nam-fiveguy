package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.advice.DuplicateException;
import com.example.demo.entity.Brand;
import com.example.demo.entity.BrandSuppiler;
import com.example.demo.entity.Supplier;
import com.example.demo.model.request.thuong.SupplierRequest;
import com.example.demo.repository.thuong.BrandRepository;
import com.example.demo.repository.thuong.SupplierRepository;
import com.example.demo.service.thuong.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private SupplierRepository repository;

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Page<Supplier> getSuppliers(int page, int size, String name, String sortField, String sortDirection) {
        Sort sort = Sort.by(sortField);
        if ("DESC".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        if (name == null || name.isEmpty())
            return repository.findAll(pageable);
        else return repository.findByNameContainingIgnoreCase(name,pageable);
    }

    @Override
    public Supplier findById(Long id) {
        return repository.findById(id).orElse(null);    }

    @Override
    public Supplier create(SupplierRequest request) {
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
            Brand existingBrand = brandRepository.findById(brand.getId()).orElse(null);
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
    public Supplier update(SupplierRequest request, Long id) {
        return null;
    }

    @Override
    public Supplier updateStatus(Long id) {
        return null;
    }

    @Override
    public Supplier delete(Long id) {
        return null;
    }
}
