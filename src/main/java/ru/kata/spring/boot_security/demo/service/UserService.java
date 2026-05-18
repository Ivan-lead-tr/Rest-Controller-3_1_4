package ru.kata.spring.boot_security.demo.service;



import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {


    void saveUser(String firstName, String lastName,
                  String email, Byte age, String password);

    List<User> getAllUsers();

    void updateUser( Long id,String firstName, String lastName,
                    String email, Byte age, String password);

    void deleteUser(Long id);

    User userByEmail(String email);

}
