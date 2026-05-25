package ru.kata.spring.boot_security.demo.repositories;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleRepository {
    Role findById(Long id);

    List<Role> findRolesByIds(Set<Long> roleIds);
}
