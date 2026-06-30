package ru.kata.spring.boot_security.demo.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dtos.UserDto;
import ru.kata.spring.boot_security.demo.mappers.UserMapper;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class AdminRestController {

    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {

        return userMapper.toDtoList(userService.getAllUsers());

    }

    @PostMapping
    public ResponseEntity<UserDto> addUsers(@RequestBody UserDto userDto){

        User addUser = userService.saveUser(userDto);
        return ResponseEntity.ok(userMapper.toDto(addUser));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUsers(@PathVariable("id") Long id) {

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUsers(@PathVariable("id") Long id,
                                               @RequestBody UserDto userDto) {
        User updateUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(userMapper.toDto(updateUser));

    }

}
