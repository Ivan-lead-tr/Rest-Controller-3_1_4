package ru.kata.spring.boot_security.demo.service;



import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {


    void saveUser(User user, List<Long> roleIds);

    List<User> getAllUsers();

    void updateUser(User user, List<Long> roleIds);

    void deleteUser(Long id);

    User userByEmail(String email);

    User findUserById(Long id);

}
