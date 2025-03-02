package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Data
public class BookingForItemResponseDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private UserResponseDto booker;
}
