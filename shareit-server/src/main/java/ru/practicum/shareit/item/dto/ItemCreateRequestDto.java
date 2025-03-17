package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class ItemCreateRequestDto {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
