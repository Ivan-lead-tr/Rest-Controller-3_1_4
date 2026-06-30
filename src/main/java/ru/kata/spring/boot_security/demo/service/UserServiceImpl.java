package ru.kata.spring.boot_security.demo.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.boot_security.demo.dtos.RoleDto;
import ru.kata.spring.boot_security.demo.dtos.UserDto;
import ru.kata.spring.boot_security.demo.mappers.UserMapper;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Transactional
    @Override
    public User saveUser(UserDto userDto) {

        Set<Long> roleIds = userDto.getRoles().stream()
                .map(RoleDto::getId)
                .collect(Collectors.toSet());

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        List<Role> roles = roleService.findAllByRoleIdIn(roleIds);
        user.setRoles(roles);

       return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {

        return userRepository.findAllWithRoles();
    }

    @Transactional
    @Override
    public User updateUser(Long id, UserDto userDto) {

        User user = userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));


        Set<Long> roleIds = userDto.getRoles().stream()
                .map(RoleDto::getId)
                .collect(Collectors.toSet());

        List<Role> roles = roleService.findAllByRoleIdIn(roleIds);

        userMapper.updateEntityFromDto(userDto, user, new HashSet<>(roles));

        if (userDto.getPassword() != null && !userDto.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

       return userRepository.save(user);

    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id)
                .ifPresent(user -> userRepository.deleteById(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> userByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findByIdWithRoles(id);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("loadUserByUsername вызван с параметром: {}", username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        logger.debug("Найден пользователь: {}", user.getEmail());
        return user;
    }
}
