package ru.kata.spring.boot_security.demo.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RoleDto {

    private Long id;

    private String name;
}
