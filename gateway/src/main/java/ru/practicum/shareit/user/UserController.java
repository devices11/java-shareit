package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;


    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        log.info("Получен запрос на получение пользователя с ID: {}", id);

        ResponseEntity<Object> foundUser = userClient.findById(id);

        log.info("Пользователь найден: {}", foundUser);
        return foundUser;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@Valid @RequestBody UserCreateRequestDto userRequestDto) {
        log.info("Получен запрос на создание пользователя: {}", userRequestDto);

        ResponseEntity<Object> createdUser = userClient.create(userRequestDto);

        log.info("Пользователь успешно создан: {}", createdUser);
        return createdUser;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id,
                                         @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        log.info("Получен запрос на обновление пользователя с ID: {}, данные для обновления: {}", id, userUpdateRequestDto);

        ResponseEntity<Object> updatedUser = userClient.update(id, userUpdateRequestDto);

        log.info("Пользователь с ID: {} успешно обновлен: {}", id, updatedUser);
        return updatedUser;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("Получен запрос на удаление пользователя с ID: {}", id);

        userClient.delete(id);

        log.info("Пользователь с ID: {} успешно удален", id);
    }
}
