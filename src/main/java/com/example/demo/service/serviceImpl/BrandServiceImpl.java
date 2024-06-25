package com.example.demo.service.serviceImpl;

import com.example.demo.advice.DuplicateException;
import com.example.demo.entity.Brand;
import com.example.demo.model.request.BrandRequest;
import com.example.demo.repository.BrandRepository;
import com.example.demo.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository repository;
    @Override
    public Page<Brand> getBrands(int page, int size, String name, String sortField, String sortDirection) {
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
    public Brand findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Brand create(BrandRequest request) {
        Brand existingBrand= repository.findByName(request.getName());
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
    public Brand update(BrandRequest request, Long id) {
        Brand existingBrand = repository.findByName(request.getName());
        if (existingBrand != null && id != existingBrand.getId()) {
            throw new DuplicateException("Trùng tên thương hiệu", "name");
        }
        Optional<Brand> brandOptional = repository.findById(id);
        if (brandOptional.isPresent()) {
            Brand brand = brandOptional.get();
            brand.setName(request.getName());
            brand.setUpdatedAt(new Date());
            brand.setStatus(1);
            return repository.save(brand);
        }
        return null;
    }

    @Override
    public Brand updateStatus(Long id) {
        return null;
    }

    @Override
    public Brand delete(Long id) {
        return null;
    }
}
