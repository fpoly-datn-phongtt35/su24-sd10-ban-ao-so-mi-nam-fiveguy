package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.advice.DuplicateException;
import com.example.demo.entity.Collar;
import com.example.demo.model.request.thuong.CollarRequestTH;
import com.example.demo.repository.thuong.CollarRepositoryTH;
import com.example.demo.service.thuong.CollarServiceTH;
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
public class CollarServiceTHImpl implements CollarServiceTH {

    @Autowired
    private CollarRepositoryTH repository;

    @Override
    public Page<Collar> getCollars(int page, int size, String name, String sortField, String sortDirection) {
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
    public List<Collar> findAllByStatus(Integer status) {
        return repository.findAllByStatus(status);
    }

    @Override
    public Collar findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Collar create(CollarRequestTH request) {
        Collar existingCollar = repository.findByName(request.getName());
        if (existingCollar != null) {
            throw new DuplicateException("Trùng tên cổ áo", "name");
        }
        Collar collar = new Collar();
        collar.setName(request.getName());
        collar.setCreatedAt(new Date());
        collar.setStatus(1);
        return repository.save(collar);
    }

    @Override
    public Collar update(CollarRequestTH request, Long id) {
        Collar existingCollar = repository.findByName(request.getName());
        if (existingCollar != null && id != existingCollar.getId()) {
            throw new DuplicateException("Trùng tên cổ áo", "name");
        }
        Optional<Collar> collarOptional = repository.findById(id);
        if (collarOptional.isPresent()) {
            Collar collar = collarOptional.get();
            collar.setName(request.getName());
            collar.setUpdatedAt(new Date());
            collar.setStatus(1);
            return repository.save(collar);
        }
        return null;
    }

    @Override
    public Collar updateStatus(Long id) {
        Optional<Collar> colarOptional = repository.findById(id);
        if (colarOptional.isPresent()) {
            Collar collar = colarOptional.get();
            collar.setUpdatedAt(new Date());
            collar.setStatus(1);
            return repository.save(collar);
        }
        return null;
    }

    @Override
    public Collar delete(Long id) {
        Optional<Collar> collarOptional = repository.findById(id);
        if (collarOptional.isPresent()) {
            Collar category = collarOptional.get();
            category.setStatus(0);
            return repository.save(category);
        }
        return null;
    }
}
