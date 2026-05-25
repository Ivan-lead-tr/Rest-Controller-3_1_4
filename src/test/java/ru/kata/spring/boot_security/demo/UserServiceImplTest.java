package ru.kata.spring.boot_security.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private UserServiceImpl userService;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        // Создаем чистые заглушки (моки) без поднятия Spring
        userRepository = Mockito.mock(UserRepository.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        userService = new UserServiceImpl(userRepository,roleService, passwordEncoder);
    }

    // 1. saveUser — проверяет что пароль зашифрован и юзер сохранён
    @Test
    void testSaveUser_ShouldEncodePasswordAndSaveUser() {
        // Given
        User user = new User("Иван", "Иванов", "ivan@mail.ru", (byte) 25);
        user.setPassword("rawPassword");
        Set<Long> roleIds = Set.of(1L);
        Role mockRole = new Role(1L, "ROLE_USER");

        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPasswordHash");
        when(roleService.findById(1L)).thenReturn(mockRole);

        // When
        userService.saveUser(user, roleIds);

        // Then
        assertEquals("encodedPasswordHash", user.getPassword(), "Пароль должен быть зашифрован");
        assertNotNull(user.getRoles(), "Роли должны быть установлены");
        verify(userRepository, times(1)).saveUser(user); // Проверяем, что юзер сохранен в репозиторий
    }

    // 2. getAllUsers — проверяет что возвращается список из репозитория
    @Test
    void testGetAllUsers_ShouldReturnListFromRepository() {
        // Given
        List<User> mockUsers = Arrays.asList(
                new User("Иван", "Иванов", "ivan@mail.ru", (byte) 25),
                new User("Пётр", "Петров", "petr@mail.ru", (byte) 30)
        );
        when(userRepository.getAllUsers()).thenReturn(mockUsers);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertNotNull(result, "Список не должен быть null");
        assertEquals(2, result.size(), "Должно вернуться 2 пользователя");
        assertEquals("Иван", result.get(0).getFirstName());
        verify(userRepository, times(1)).getAllUsers(); // Проверяем, что вызван именно репозиторий
    }

    // 3. updateUser — проверяет что поля обновились, пароль перешифровался, вызван updateUser
    @Test
    void testUpdateUser_ShouldUpdateFieldsEncodePasswordAndCallRepository() {
        // Given
        User formUser = new User();
        formUser.setId(1L);
        formUser.setFirstName("НовоеИмя");
        formUser.setLastName("НоваяФамилия");
        formUser.setEmail("new@mail.ru");
        formUser.setAge((byte) 26);
        formUser.setPassword("newRawPassword"); // Новый пароль для перешифрования

        User dbUser = new User("СтароеИмя", "СтараяФамилия", "old@mail.ru", (byte) 25);
        dbUser.setPassword("oldSecretHash");

        when(userRepository.findUserById(1L)).thenReturn(dbUser);
        when(passwordEncoder.encode("newRawPassword")).thenReturn("newEncodedHash");

        // When
        userService.updateUser(formUser, null);

        // Then
        assertEquals("НовоеИмя", dbUser.getFirstName(), "Имя должно обновиться");
        assertEquals("НоваяФамилия", dbUser.getLastName(), "Фамилия должна обновиться");
        assertEquals("new@mail.ru", dbUser.getEmail(), "Email должен обновиться");
        assertEquals((byte) 26, dbUser.getAge(), "Возраст должен обновиться");
        assertEquals("newEncodedHash", dbUser.getPassword(), "Пароль должен перешифроваться");

        verify(userRepository, times(1)).updateUser(dbUser); // Проверяем, что вызван updateUser репозитория
    }

    // 4. deleteUser — проверяет что вызван deleteUser с нужным id
    @Test
    void testDeleteUser_ShouldCallRepositoryWithCorrectId() {
        // Given
        Long userIdToDelete = 5L;

        // When
        userService.deleteUser(userIdToDelete);

        // Then
        verify(userRepository, times(1)).deleteUser(userIdToDelete); // Проверяем вызов репозитория с нужным ID
    }
}
