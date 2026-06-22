package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long>{

    List<Role> findAllByIdIn(Set<Long> roleIds);
}
