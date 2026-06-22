package ru.kata.spring.boot_security.demo.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dtos.RoleDto;
import ru.kata.spring.boot_security.demo.dtos.UserDto;
import ru.kata.spring.boot_security.demo.mappers.RoleMapper;
import ru.kata.spring.boot_security.demo.mappers.UserMapper;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

    private final UserMapper userMapper;
    private final UserService userService;
    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @GetMapping
    public List<UserDto> getAllUsers() {

        return userMapper.toDtoList(userService.getAllUsers());

    }

    @PostMapping
    public ResponseEntity<UserDto> addUsers(@RequestBody UserDto userDto){

        Set<Long> roleIds = userDto.getRoles().stream()
                .map(RoleDto::getId)
                .collect(Collectors.toSet());

        User user = userMapper.toEntity(userDto);
        userService.saveUser(user, roleIds);

        return ResponseEntity.ok(userMapper.toDto(user));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUsers(@PathVariable("id") Long id) {

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUsers(@PathVariable("id") Long id,
                                               @RequestBody UserDto userDto) {

       User user = userService.findUserById(id)
               .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

       Set<Long> roleIds = userDto.getRoles().stream()
               .map(RoleDto::getId)
               .collect(Collectors.toSet());
        // Находим полноценные сущности Role по полученным ID
        List<Role> rolesList = roleService.findAllByRoleIdIn(roleIds);
        Set<Role> roles = new HashSet<>(rolesList);

        userMapper.updateEntityFromDto(userDto, user, roles);

        User updatedUser = userService.updateUser(user, roleIds);

       return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

}
