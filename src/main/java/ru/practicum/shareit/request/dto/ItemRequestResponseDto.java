package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemForRequestorResponseDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class ItemRequestResponseDto {
    private int id;
    private String description;
    private LocalDateTime created;
    private Collection<ItemForRequestorResponseDto> items;
}
