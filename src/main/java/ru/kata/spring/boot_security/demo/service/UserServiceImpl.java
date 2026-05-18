package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;


import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void saveUser (String firstName, String lastName,
                          String email, Byte age, String password) {
        User user = new User(firstName, lastName, email, age);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.saveUser(user);

    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {

        return userRepository.getAllUsers();
    }

    @Transactional
    @Override
    public void updateUser(Long id,String firstName, String lastName,
                           String email, Byte age, String password) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAge(age);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.updateUser(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {

        userRepository.deleteUser(id);
    }

    @Transactional(readOnly = true)
    @Override
    public User userByEmail(String email) {
       return userRepository.userByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.userByEmail(username);
        if (user == null){
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return user;
    }
}
