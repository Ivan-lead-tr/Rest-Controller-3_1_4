package ru.kata.spring.boot_security.demo.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dtos.UserDto;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserMapper {

    private final RoleMapper roleMapper;

//Отдаем клиенту
    public UserDto toDto(User user) {

        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setRoles(roleMapper.toDtoSet(user.getRoles()));

        return dto;
    }
//Создаем юзера
    public User toEntity(UserDto userDto) {

        if (userDto ==null)  {

            return null;
        }

        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());
        user.setRoles(roleMapper.toEntitySet(userDto.getRoles()));

        return user;
    }

    //Отдаем всех юзеров
    public List<UserDto> toDtoList(List<User> users) {

        if (users == null) {
            return Collections.emptyList();
        }

        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void updateEntityFromDto (UserDto userDto, User user, Set<Role> roles) {

        if (userDto == null || user == null) {
            return;
        }

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());
        user.setRoles(roles);

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(userDto.getPassword());
        }
    }
}
