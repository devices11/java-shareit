package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequestDto {
    @NotNull(message = "id вещи не может быть пустым")
    private Long itemId;

    @NotNull(message = "Дата бронирования вещи не может быть пустой")
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull(message = "Дата окончания бронирования вещи не может быть пустой")
    @Future
    private LocalDateTime end;
}
