package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.util.validation.groups.Create;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
@Data
public class ItemDto {
    private Long id;

    @NotBlank(groups = Create.class, message = "Название вещи не может быть пустым")
    private String name;

    @NotBlank(groups = Create.class, message = "Описание вещи не может быть пустым")
    private String description;

    @NotNull(groups = Create.class, message = "Статус доступности не может быть null")
    private Boolean available;

    private Long requestId;
}
