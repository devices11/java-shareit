package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingForItemResponseDto;

import java.util.Collection;

@Builder(toBuilder = true)
@Data
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingForItemResponseDto lastBooking;
    private BookingForItemResponseDto nextBooking;
    private Collection<CommentResponseDto> comments;
    private Long requestId;
}
