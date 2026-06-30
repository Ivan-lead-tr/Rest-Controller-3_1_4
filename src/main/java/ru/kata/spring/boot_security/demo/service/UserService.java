package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.dtos.UserDto;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(UserDto userDto);

    List<User> getAllUsers();

    User updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    Optional<User> userByEmail(String email);

    Optional<User> findUserById(Long id);;
}
