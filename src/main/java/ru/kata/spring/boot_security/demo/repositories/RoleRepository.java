package ru.kata.spring.boot_security.demo.repositories;

import ru.kata.spring.boot_security.demo.model.Role;

public interface RoleRepository {
    Role findById(Long id);
}
