package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.advice.DuplicateException;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Category;
import com.example.demo.model.request.thuong.BrandRequestTH;
import com.example.demo.repository.thuong.BrandRepositoryTH;
import com.example.demo.service.thuong.BrandServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceTHImpl implements BrandServiceTH {
    @Autowired
    private BrandRepositoryTH repository;

    @Override
    public Page<Brand> getBrands(int page, int size, String name, String sortField, String sortDirection, Integer status) {
        Sort sort = Sort.by(sortField);
        if ("DESC".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        if (name == null || name.isEmpty())
            return repository.findAllAndStatus(status, pageable);
        return repository.findByNameContainingIgnoreCaseAndStatus(name, status, pageable);
    }

    @Override
    public List<Brand> findAllByStatus(Integer status) {
        return repository.findAllByStatus(status);
    }

    @Override
    public Brand findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Brand create(BrandRequestTH request) {
        Brand existingBrand = repository.findByName(request.getName());
        if (existingBrand != null) {
            throw new DuplicateException("Trùng tên thương hiệu", "name");
        }
        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setCreatedAt(new Date());
        brand.setStatus(1);
        return repository.save(brand);
    }

    @Override
    public Brand update(BrandRequestTH request, Long id) {
        Brand existingBrand = repository.findByName(request.getName());
        if (existingBrand != null && id != existingBrand.getId()) {
            throw new DuplicateException("Trùng tên thương hiệu", "name");
        }
        Optional<Brand> brandOptional = repository.findById(id);
        if (brandOptional.isPresent()) {
            Brand brand = brandOptional.get();
            brand.setName(request.getName());
            brand.setUpdatedAt(new Date());
            return repository.save(brand);
        }
        return null;
    }

    @Override
    public Brand updateStatus(Long id) {
        Optional<Brand> brandOptional = repository.findById(id);
        if (brandOptional.isPresent()) {
            Brand brand = brandOptional.get();
            brand.setUpdatedAt(new Date());
            brand.setStatus(1);
            return repository.save(brand);
        }
        return null;
    }

    @Override
    public Brand delete(Long id) {
        Optional<Brand> brandOptional = repository.findById(id);
        if (brandOptional.isPresent()) {
            Brand brand = brandOptional.get();
            brand.setStatus(0);
            return repository.save(brand);
        }
        return null;
    }
}
