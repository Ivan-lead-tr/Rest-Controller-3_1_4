package ru.kata.spring.boot_security.demo.repositories;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void saveUser(User user);

    List<User> getAllUsers();

    void updateUser(User user);

    void deleteUser(Long id);

    Optional<User> userByEmail(String email);

    Optional<User> findUserById(Long id);
}
