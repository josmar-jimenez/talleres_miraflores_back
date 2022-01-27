package com.inventory_system.backend.service;

import com.inventory_system.backend.model.Role;
import com.inventory_system.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> findById(int id) {
        return roleRepository.findById(id);
    }
}
