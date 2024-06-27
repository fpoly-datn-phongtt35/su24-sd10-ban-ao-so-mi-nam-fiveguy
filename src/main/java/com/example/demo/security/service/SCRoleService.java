package com.example.demo.security.service;

import com.example.demo.entity.Role;

import java.util.List;
import java.util.Optional;

public interface SCRoleService {

    List<Role> getAll();

    Role save(Role roles);

    void delete(Long id);

    Role update(Long id, Role roles);

    Optional<Role> findByFullNameAndStatus(String fullName, int status);
}
