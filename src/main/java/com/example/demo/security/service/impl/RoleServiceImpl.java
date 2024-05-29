package com.example.demo.security.service.impl;

import com.example.demo.entity.Role;
import com.example.demo.security.repository.RoleRepository;
import com.example.demo.security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service

public class RoleServiceImpl implements RoleService {
    @Autowired
   private RoleRepository roleRepository;

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role save(Role roles) {
        return roleRepository.save(roles);
    }

    @Override
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role update(Long id, Role roles) {
        Optional<Role> rolesOptional = roleRepository.findById(id);
        if (rolesOptional.isPresent()) {
            Role roles1 = rolesOptional.get();
            roles1.setFullName(roles.getFullName());
            roles1.setCreatedAt(roles.getCreatedAt());
            roles1.setUpdatedAt(roles.getUpdatedAt());
            roles1.setStatus(roles.getStatus());

            return roleRepository.save(roles1);
        } else {
            throw new IllegalArgumentException("Không tìm thấy khách hàng với ID " + id);
        }
    }

    @Override
    public Optional<Role> findByFullNameAndStatus(String fullName, int status) {
        Optional<Role> roles = roleRepository.findByFullNameAndStatus(fullName, 1);
        if (roles.isPresent()) {
            return roles;
        }
        return Optional.empty();
    }
}