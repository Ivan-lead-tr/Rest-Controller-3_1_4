package ru.kata.spring.boot_security.demo.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class RoleDto {

    private Long id;

    private String name;
}
