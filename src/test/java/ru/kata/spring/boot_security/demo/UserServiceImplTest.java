package ru.kata.spring.boot_security.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kata.spring.boot_security.demo.dtos.RoleDto;
import ru.kata.spring.boot_security.demo.dtos.UserDto;
import ru.kata.spring.boot_security.demo.mappers.UserMapper;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private UserServiceImpl userService;
    private RoleService roleService;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        // Создаем чистые заглушки (моки) без поднятия Spring
        userRepository = Mockito.mock(UserRepository.class);
        roleService = Mockito.mock(RoleService.class);
        userMapper = Mockito.mock(UserMapper.class);
        passwordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, roleService, userMapper, passwordEncoder);
    }

    // 1. saveUser — проверяет что пароль зашифрован и юзер сохранён
    @Test
    void testSaveUser_ShouldEncodePasswordAndSaveUser() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setFirstName("Иван");
        userDto.setLastName("Иванов");
        userDto.setEmail("ivan@mail.ru");
        userDto.setAge((byte) 25);
        userDto.setPassword("rawPassword");

        RoleDto roleDto = new RoleDto();
        roleDto.setId(1L);
        userDto.setRoles(Set.of(roleDto));

        Set<Long> roleIds = Set.of(1L);
        Role mockRole = new Role(1L, "ROLE_USER");

        User mappedUser = new User("Иван", "Иванов", "ivan@mail.ru", (byte) 25);
        mappedUser.setPassword("rawPassword");

        when(userMapper.toEntity(userDto)).thenReturn(mappedUser);
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPasswordHash");
        when(roleService.findAllByRoleIdIn(roleIds)).thenReturn(List.of(mockRole));;

        // When
        userService.saveUser(userDto);

        // Then
        assertEquals("encodedPasswordHash", mappedUser.getPassword(), "Пароль должен быть зашифрован");
        assertNotNull(mappedUser.getRoles(), "Роли должны быть установлены");
        verify(userRepository, times(1)).save(mappedUser); // Проверяем, что юзер сохранен в репозиторий
    }

    // 2. getAllUsers — проверяет что возвращается список из репозитория
    @Test
    void testGetAllUsers_ShouldReturnListFromRepository() {
        // Given
        List<User> mockUsers = Arrays.asList(
                new User("Иван", "Иванов", "ivan@mail.ru", (byte) 25),
                new User("Пётр", "Петров", "petr@mail.ru", (byte) 30)
        );
        when(userRepository.findAllWithRoles()).thenReturn(mockUsers);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertNotNull(result, "Список не должен быть null");
        assertEquals(2, result.size(), "Должно вернуться 2 пользователя");
        assertEquals("Иван", result.get(0).getFirstName());
        verify(userRepository, times(1)).findAllWithRoles(); // Проверяем, что вызван именно репозиторий
    }

    // 3. updateUser — проверяет что поля обновились, пароль перешифровался, вызван updateUser
    @Test
    void testUpdateUser_ShouldUpdateFieldsEncodePasswordAndCallRepository() {
            // Given
            UserDto userDto = new UserDto();
            userDto.setFirstName("НовоеИмя");
            userDto.setLastName("НоваяФамилия");
            userDto.setEmail("new@mail.ru");
            userDto.setAge((byte) 26);
            userDto.setPassword("newRawPassword");

            RoleDto roleDto = new RoleDto();
            roleDto.setId(1L);
            userDto.setRoles(Set.of(roleDto));

            User dbUser = new User("СтароеИмя", "СтараяФамилия", "old@mail.ru", (byte) 25);
            dbUser.setId(1L);
            dbUser.setPassword("oldSecretHash");

            Set<Long> roleIds = Set.of(1L);
            Role mockRole = new Role(1L, "ROLE_USER");

            when(userRepository.findByIdWithRoles(1L)).thenReturn(Optional.of(dbUser));
            when(roleService.findAllByRoleIdIn(roleIds)).thenReturn(List.of(mockRole));
            when(passwordEncoder.encode("newRawPassword")).thenReturn("newEncodedHash");

            doAnswer(invocation -> {
                UserDto dto = invocation.getArgument(0);
                User entity = invocation.getArgument(1);
                entity.setFirstName(dto.getFirstName());
                entity.setLastName(dto.getLastName());
                entity.setEmail(dto.getEmail());
                entity.setAge(dto.getAge());
                return null;
            }).when(userMapper).updateEntityFromDto(eq(userDto), eq(dbUser), any());

            when(userRepository.save(dbUser)).thenReturn(dbUser);

            // When
            userService.updateUser(1L, userDto);

            // Then
            assertEquals("НовоеИмя", dbUser.getFirstName(), "Имя должно обновиться");
            assertEquals("НоваяФамилия", dbUser.getLastName(), "Фамилия должна обновиться");
            assertEquals("new@mail.ru", dbUser.getEmail(), "Email должен обновиться");
            assertEquals((byte) 26, dbUser.getAge(), "Возраст должен обновиться");
            assertEquals("newEncodedHash", dbUser.getPassword(), "Пароль должен перешифроваться");

            verify(userRepository, times(1)).save(dbUser);
        }

    // 4. deleteUser — проверяет что вызван deleteUser с нужным id
    @Test
    void testDeleteUser_ShouldCallRepositoryWithCorrectId() {
        // Given
        Long userIdToDelete = 5L;
        User userToDelete = new User("Иван", "Иванов", "ivan@mail.ru", (byte) 25);
        userToDelete.setId(userIdToDelete);

        // Настраиваем мок: при поиске пользователя с id=5L возвращаем Optional с пользователем
        when(userRepository.findById(userIdToDelete)).thenReturn(Optional.of(userToDelete));

        // When
        userService.deleteUser(userIdToDelete);

        // Then
        verify(userRepository, times(1)).findById(userIdToDelete);
        verify(userRepository, times(1)).deleteById(userIdToDelete);
    }
}
