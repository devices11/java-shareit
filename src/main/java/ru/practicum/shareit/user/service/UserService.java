package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;

public interface UserService {
    UserResponseDto findById(Long id);

    UserResponseDto create(UserCreateRequestDto userCreateRequestDto);

    UserResponseDto update(Long userId, UserUpdateRequestDto userUpdateRequestDto);

    void delete(Long id);
}