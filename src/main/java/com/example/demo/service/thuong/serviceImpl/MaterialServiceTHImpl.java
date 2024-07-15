package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.advice.DuplicateException;
import com.example.demo.entity.Material;
import com.example.demo.model.request.thuong.MaterialRequestTH;
import com.example.demo.repository.thuong.MaterialRepositoryTH;
import com.example.demo.service.thuong.MaterialServiceTH;
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
public class MaterialServiceTHImpl implements MaterialServiceTH {
    @Autowired
    private MaterialRepositoryTH repository;

    @Override
    public Page<Material> getMaterials(int page, int size, String name, String sortField, String sortDirection) {
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
    public List<Material> lstMaterials(Integer status) {
        return repository.findAllByStatus(status);
    }

    @Override
    public Material findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Material create(MaterialRequestTH request) {
        Material existingMaterial = repository.findByName(request.getName());
        if (existingMaterial != null) {
            throw new DuplicateException("Trùng tên chất liệu", "name");
        }
        Material material = new Material();
        material.setName(request.getName());
        material.setCreatedAt(new Date());
        material.setStatus(1);
        return repository.save(material);
    }

    @Override
    public Material update(MaterialRequestTH request, Long id) {
        Material existingMaterial = repository.findByName(request.getName());
        if (existingMaterial != null && id != existingMaterial.getId()) {
            throw new DuplicateException("Trùng tên chất liệu", "name");
        }
        Optional<Material> materialOptional = repository.findById(id);
        if (materialOptional.isPresent()) {
            Material material = materialOptional.get();
            material.setName(request.getName());
            material.setUpdatedAt(new Date());
            material.setStatus(1);
            return repository.save(material);
        }
        return null;
    }

    @Override
    public Material updateStatus(Long id) {
        Optional<Material> materialOptional = repository.findById(id);
        if (materialOptional.isPresent()) {
            Material material = materialOptional.get();
            material.setUpdatedAt(new Date());
            material.setStatus(1);
            return repository.save(material);
        }
        return null;
    }

    @Override
    public Material delete(Long id) {
        Optional<Material> materialOptional = repository.findById(id);
        if (materialOptional.isPresent()) {
            Material material = materialOptional.get();
            material.setStatus(0);
            return repository.save(material);
        }
        return null;
    }
}
