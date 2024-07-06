package com.example.demo.security.service.impl;

import com.example.demo.entity.Role;
import com.example.demo.security.repository.SCRoleRepository;
import com.example.demo.security.service.SCRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service

public class SCSCRoleServiceImpl implements SCRoleService {
    @Autowired
   private SCRoleRepository SCRoleRepository;

    @Override
    public List<Role> getAll() {
        return SCRoleRepository.findAll();
    }

    @Override
    public Role save(Role roles) {
        return SCRoleRepository.save(roles);
    }

    @Override
    public void delete(Long id) {
        SCRoleRepository.deleteById(id);
    }

    @Override
    public Role update(Long id, Role roles) {
        Optional<Role> rolesOptional = SCRoleRepository.findById(id);
        if (rolesOptional.isPresent()) {
            Role roles1 = rolesOptional.get();
            roles1.setFullName(roles.getFullName());
            roles1.setCreatedAt(roles.getCreatedAt());
            roles1.setUpdatedAt(roles.getUpdatedAt());
            roles1.setStatus(roles.getStatus());

            return SCRoleRepository.save(roles1);
        } else {
            throw new IllegalArgumentException("Không tìm thấy khách hàng với ID " + id);
        }
    }

    @Override
    public Optional<Role> findByFullNameAndStatus(String fullName, int status) {
        Optional<Role> roles = SCRoleRepository.findByFullNameAndStatus(fullName, 1);
        if (roles.isPresent()) {
            return roles;
        }
        return Optional.empty();
    }
}