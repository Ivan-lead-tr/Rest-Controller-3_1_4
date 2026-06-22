package ru.kata.spring.boot_security.demo.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dtos.RoleDto;
import ru.kata.spring.boot_security.demo.mappers.RoleMapper;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.HashSet;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/roles")
public class RoleRestController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @GetMapping
    public List<RoleDto> getAllRoles() {

        return roleMapper.toDtoSet(new HashSet<>(roleService.findAll()))
                .stream()
                .toList();
    }

}
