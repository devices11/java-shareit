package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;


    @GetMapping("/{id}")
    public UserResponseDto findById(@PathVariable Long id) {
        log.info("Получен запрос на получение пользователя с ID: {}", id);

        UserResponseDto foundUser = userService.findById(id);

        log.info("Пользователь найден: {}", foundUser);
        return foundUser;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(@Validated @RequestBody UserCreateRequestDto userRequestDto) {
        log.info("Получен запрос на создание пользователя: {}", userRequestDto);

        UserResponseDto createdUser = userService.create(userRequestDto);

        log.info("Пользователь успешно создан: {}", createdUser);
        return createdUser;
    }

    @PatchMapping("/{id}")
    public UserResponseDto update(@PathVariable Long id,
                                  @Validated @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        log.info("Получен запрос на обновление пользователя с ID: {}, данные для обновления: {}", id, userUpdateRequestDto);

        UserResponseDto updatedUser = userService.update(id, userUpdateRequestDto);

        log.info("Пользователь с ID: {} успешно обновлен: {}", id, updatedUser);
        return updatedUser;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("Получен запрос на удаление пользователя с ID: {}", id);

        userService.delete(id);

        log.info("Пользователь с ID: {} успешно удален", id);
    }
}
