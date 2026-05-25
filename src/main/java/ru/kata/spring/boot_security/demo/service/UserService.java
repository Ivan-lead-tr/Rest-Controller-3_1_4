package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {


    void saveUser(User user, Set<Long> roleIds);

    List<User> getAllUsers();

    void updateUser(User user, Set<Long> roleIds);

    void deleteUser(Long id);

    User userByEmail(String email);

    User findUserById(Long id);

}
