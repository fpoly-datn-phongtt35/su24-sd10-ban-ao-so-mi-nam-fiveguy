package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.advice.DuplicateException;
import com.example.demo.entity.Size;
import com.example.demo.model.request.thuong.SizeRequestTH;
import com.example.demo.repository.thuong.SizeRepositoryTH;
import com.example.demo.service.thuong.SizeServiceTH;
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
public class SizeServiceTHImpl implements SizeServiceTH {
    @Autowired
    private SizeRepositoryTH repository;

    @Override
    public Page<Size> getSizes(int page, int size, String name, String sortField, String sortDirection, Integer status) {
        Sort sort = Sort.by(sortField);
        if ("DESC".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        if (name == null || name.isEmpty())
            return repository.findAllAndStatus(status, pageable);
        else return repository.findByNameContainingIgnoreCaseAndStatus(name, status, pageable);
    }

    @Override
    public List<Size> findAllByStatus(Integer status) {
        return repository.findAllByStatus(status);
    }

    @Override
    public Size findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Size create(SizeRequestTH request) {
        Size existingMaterial = repository.findByName(request.getName());
        if (existingMaterial != null) {
            throw new DuplicateException("Trùng tên size", "name");
        }
        Size size = new Size();
        size.setName(request.getName());
        size.setCreatedAt(new Date());
        size.setStatus(1);
        return repository.save(size);
    }

    @Override
    public Size update(SizeRequestTH request, Long id) {
        Size existingSize = repository.findByName(request.getName());
        if (existingSize != null && id != existingSize.getId()) {
            throw new DuplicateException("Trùng tên size", "name");
        }
        Optional<Size> sizeOptional = repository.findById(id);
        if (sizeOptional.isPresent()) {
            Size size = sizeOptional.get();
            size.setName(request.getName());
            size.setUpdatedAt(new Date());
            size.setStatus(1);
            return repository.save(size);
        }
        return null;
    }

    @Override
    public Size updateStatus(Long id) {
        Optional<Size> sizeOptional = repository.findById(id);
        if (sizeOptional.isPresent()) {
            Size size = sizeOptional.get();
            size.setUpdatedAt(new Date());
            size.setStatus(1);
            return repository.save(size);
        }
        return null;
    }

    @Override
    public Size delete(Long id) {
        Optional<Size> sizeOptional = repository.findById(id);
        if (sizeOptional.isPresent()) {
            Size size = sizeOptional.get();
            size.setStatus(0);
            return repository.save(size);
        }
        return null;
    }
}
