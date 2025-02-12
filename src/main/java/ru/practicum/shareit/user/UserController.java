package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.validation.groups.Create;
import ru.practicum.shareit.util.validation.groups.Update;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto findById(
            @PathVariable Long id
    ) {
        log.info("Получен запрос на получение пользователя с ID: {}", id);

        User user = userService.findById(id);
        UserDto foundUser = UserMapper.toUserDto(user);

        log.info("Пользователь найден: {}", foundUser);
        return foundUser;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(
            @Validated(Create.class) @RequestBody UserDto userDto
    ) {
        log.info("Получен запрос на создание пользователя: {}", userDto);

        User user = UserMapper.toUser(userDto);
        User createdUser = userService.create(user);
        UserDto createdUserDto = UserMapper.toUserDto(createdUser);

        log.info("Пользователь успешно создан: {}", createdUserDto);
        return createdUserDto;
    }

    @PatchMapping("/{id}")
    public UserDto update(
            @PathVariable Long id,
            @Validated(Update.class) @RequestBody UserDto userDto
    ) {
        log.info("Получен запрос на обновление пользователя с ID: {}, данные для обновления: {}", id, userDto);

        User user = UserMapper.toUser(userDto);
        User updatedUser = userService.update(user.toBuilder().id(id).build());
        UserDto updatedUserDto = UserMapper.toUserDto(updatedUser);

        log.info("Пользователь с ID: {} успешно обновлен: {}", id, updatedUserDto);
        return updatedUserDto;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id
    ) {
        log.info("Получен запрос на удаление пользователя с ID: {}", id);

        userService.delete(id);

        log.info("Пользователь с ID: {} успешно удален", id);
    }
}
