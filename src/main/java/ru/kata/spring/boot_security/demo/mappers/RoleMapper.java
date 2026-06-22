package ru.kata.spring.boot_security.demo.mappers;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dtos.RoleDto;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    public RoleDto toDto(Role role) {
        if (role == null) {
            return null;
        }

        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setName(role.getName());
        return roleDto;
    }

    public Role toEntity(RoleDto dto) {
        if (dto == null) return null;
        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName());
        return role;
    }

    public Set<RoleDto> toDtoSet(Set<Role> roles) {
        if (roles == null) {
            return Collections.emptySet();
        }
        return  roles.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    public Set<Role> toEntitySet(Set<RoleDto> roleDtos) {

        if (roleDtos == null) {
            return Collections.emptySet();
        }

        return roleDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }
}
