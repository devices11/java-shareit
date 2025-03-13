package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemRequestCreateDto {
    @NotNull(message = "Описание не может быть пустым")
    private String description;
}
