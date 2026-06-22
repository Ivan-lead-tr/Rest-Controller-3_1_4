package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    void saveUser(User user, Set<Long> roleIds);

    List<User> getAllUsers();

    User updateUser(User user, Set<Long> roleIds);

    void deleteUser(Long id);

    Optional<User> userByEmail(String email);

    Optional<User> findUserById(Long id);;
}
