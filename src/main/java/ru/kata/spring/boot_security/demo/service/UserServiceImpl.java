package ru.kata.spring.boot_security.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void saveUser(User user, Set<Long> roleIds) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> roles = roleService.findRolesByIds(roleIds);
        user.setRoles(roles);

        userRepository.saveUser(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {

        return userRepository.getAllUsers();
    }

    @Transactional
    @Override
    public void updateUser(User user, Set<Long> roleIds) {
        User udUser = userRepository.findUserById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        udUser.setFirstName(user.getFirstName());
        udUser.setLastName(user.getLastName());
        udUser.setEmail(user.getEmail());
        udUser.setAge(user.getAge());

        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            udUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        List<Role> roles = roleService.findRolesByIds(roleIds);
        udUser.setRoles(roles);

        userRepository.updateUser(udUser);

    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userRepository.findUserById(id)
                .ifPresent(user -> userRepository.deleteUser(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> userByEmail(String email) {
        return userRepository.userByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findUserById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("loadUserByUsername вызван с параметром: {}", username);
        User user = userRepository.userByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        logger.debug("Найден пользователь: {}", user.getEmail());
        return user;
    }
}
