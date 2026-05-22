package ru.kata.spring.boot_security.demo.repositories;




import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserRepository {

    void saveUser(User user);

    List<User> getAllUsers();

    void updateUser(User user);

    void deleteUser(Long id);

    User userByEmail(String email);

    Role findById(Long id);

    User findUserById(Long id);
}
