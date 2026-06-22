package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Role findByRoleId(Long id) {
        return roleRepository.findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Role> findAllByRoleIdIn(Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return emptyList();
        }
        return roleRepository.findAllByIdIn(roleIds);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
