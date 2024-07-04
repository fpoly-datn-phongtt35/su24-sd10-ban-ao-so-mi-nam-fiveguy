package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.advice.DuplicateException;
import com.example.demo.entity.Wrist;
import com.example.demo.model.request.thuong.WristRequestTH;
import com.example.demo.repository.thuong.WristRepositoryTH;
import com.example.demo.service.thuong.WristServiceTH;
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
public class WristServiceTHImpl implements WristServiceTH {
    @Autowired
    private WristRepositoryTH repository;


    @Override
    public Page<Wrist> getWrists(int page, int size, String name, String sortField, String sortDirection) {
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
    public List<Wrist> findAllByStatus(Integer status) {
        return repository.findAllByStatus(status);
    }

    @Override
    public Wrist findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Wrist create(WristRequestTH request) {
        Wrist existingWrist = repository.findByName(request.getName());
        if (existingWrist != null) {
            throw new DuplicateException("Trùng tên cổ tay", "name");
        }
        Wrist wrist = new Wrist();
        wrist.setName(request.getName());
        wrist.setCreatedAt(new Date());
        wrist.setStatus(1);
        return repository.save(wrist);
    }

    @Override
    public Wrist update(WristRequestTH request, Long id) {
        Wrist existingWrist = repository.findByName(request.getName());
        if (existingWrist != null && id != existingWrist.getId()) {
            throw new DuplicateException("Trùng tên cổ tay", "name");
        }
        Optional<Wrist> wristOptional = repository.findById(id);
        if (wristOptional.isPresent()) {
            Wrist wrist = wristOptional.get();
            wrist.setName(request.getName());
            wrist.setUpdatedAt(new Date());
            wrist.setStatus(1);
            return repository.save(wrist);
        }
        return null;
    }

    @Override
    public Wrist updateStatus(Long id) {
        Optional<Wrist> wristOptional = repository.findById(id);
        if (wristOptional.isPresent()) {
            Wrist wrist = wristOptional.get();
            wrist.setUpdatedAt(new Date());
            wrist.setStatus(1);
            return repository.save(wrist);
        }
        return null;
    }

    @Override
    public Wrist delete(Long id) {
        Optional<Wrist> wristOptional = repository.findById(id);
        if (wristOptional.isPresent()) {
            Wrist wrist = wristOptional.get();
            wrist.setStatus(0);
            return repository.save(wrist);
        }
        return null;
    }
}
