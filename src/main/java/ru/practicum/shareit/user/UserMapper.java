package ru.practicum.shareit.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    UserResponseDto toUserResponseDto(User user);

    @Mapping(target = "id", source = "userId")
    @Mapping(target = "name", source = "userUpdateRequestDto.name")
    @Mapping(target = "email", source = "userUpdateRequestDto.email")
    User toUser(Long userId, UserUpdateRequestDto userUpdateRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    User toUser(UserCreateRequestDto userCreateRequestDto);
}
