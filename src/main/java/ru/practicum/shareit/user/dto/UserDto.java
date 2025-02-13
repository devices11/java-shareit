package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.util.validation.groups.Create;
import ru.practicum.shareit.util.validation.groups.Update;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
@Data
public class UserDto {
    private Long id;

    @NotBlank(groups = Create.class, message = "Имя пользователя не может быть пустым")
    private String name;

    @Email(groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class, message = "email пользователя не может быть пустым")
    private String email;
}
