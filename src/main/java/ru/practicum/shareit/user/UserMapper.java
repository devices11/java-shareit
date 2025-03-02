package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {

    public UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User toUser(Long userId, UserUpdateRequestDto userUpdateRequestDto) {
        return User.builder()
                .id(userId)
                .name(userUpdateRequestDto.getName())
                .email(userUpdateRequestDto.getEmail())
                .build();
    }

    public User toUser(UserCreateRequestDto userRequestDto) {
        return User.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .build();
    }
}
