package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingForItemResponseDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
@Data
public class ItemOwnerResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingForItemResponseDto lastBooking;
    private BookingForItemResponseDto nextBooking;
}
