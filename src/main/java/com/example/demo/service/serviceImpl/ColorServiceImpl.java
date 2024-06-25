package com.example.demo.service.serviceImpl;

import com.example.demo.advice.DuplicateException;
import com.example.demo.entity.Color;
import com.example.demo.model.request.ColorRequest;
import com.example.demo.repository.ColorRepository;
import com.example.demo.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ColorServiceImpl implements ColorService {
    @Autowired
    private ColorRepository repository;

    @Override
    public Page<Color> getColor(int page, int size, String keyword, String sortField, String sortDirection) {
        Sort sort = Sort.by(sortField);
        if ("DESC".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        if (keyword == null || keyword.isEmpty())
            return repository.findAll(pageable);
        else return repository.findByNameAndColorCode(keyword,pageable);
    }

    @Override
    public Color findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Color create(ColorRequest request) {
        Color exColorByName = repository.findByName(request.getName());
        if (exColorByName != null) {
            throw new DuplicateException("Trùng tên màu", "name");
        }
        Color exColorByColorCode = repository.findByColorCode(request.getColorCode());
        if (exColorByColorCode != null) {
            throw new DuplicateException("Trùng mã màu", "colorCode");
        }
        Color color = new Color();
        color.setName(request.getName());
        color.setColorCode(request.getColorCode());
        color.setCreatedAt(new Date());
        color.setStatus(1);
        return repository.save(color);
    }

    @Override
    public Color update(ColorRequest request, Long id) {
        Color exColorByName = repository.findByName(request.getName());
        if (exColorByName != null && id != exColorByName.getId()) {
            throw new DuplicateException("Trùng tên màu", "name");
        }
        Color exColorByColorCode = repository.findByColorCode(request.getColorCode());
        if (exColorByColorCode != null && id != exColorByColorCode.getId()) {
            throw new DuplicateException("Trùng mã màu", "colorCode");
        }
        Optional<Color> colorOptional = repository.findById(id);
        if (colorOptional.isPresent()) {
            Color color = colorOptional.get();
            color.setName(request.getName());
            color.setColorCode(request.getColorCode());
            color.setUpdatedAt(new Date());
            color.setStatus(1);
            return repository.save(color);
        }
        return null;
    }

    @Override
    public Color updateStatus(Long id) {
        Optional<Color> colorOptional = repository.findById(id);
        if (colorOptional.isPresent()) {
            Color color = colorOptional.get();
            color.setUpdatedAt(new Date());
            color.setStatus(1);
            return repository.save(color);
        }
        return null;
    }

    @Override
    public Color delete(Long id) {
        Optional<Color> colorOptional = repository.findById(id);
        if (colorOptional.isPresent()) {
            Color color = colorOptional.get();
            color.setStatus(0);
            return repository.save(color);
        }
        return null;
    }
}
