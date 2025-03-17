package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class UserUpdateRequestDto {
    private String name;
    private String email;
}
