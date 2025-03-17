package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class UserUpdateRequestDto {
    private String name;

    @Email(message = "Некорректный формат email")
    private String email;
}
