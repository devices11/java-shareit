package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class ItemUpdateRequestDto {
    private String name;
    private String description;
    private Boolean available;
}
