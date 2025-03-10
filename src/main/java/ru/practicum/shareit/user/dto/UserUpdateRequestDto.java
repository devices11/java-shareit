package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateRequestDto {
    private String name;

    @Email(message = "Некорректный формат email")
    private String email;
}
