package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class BookingRequestDto {
    @NotNull(message = "id вещи не может быть пустым")
    private Long itemId;

    @NotNull(message = "Дата старта бронирования вещи не может быть пустой")
    @FutureOrPresent(message = "Дата старта бронирования должна быть текущей или в будущем")
    private LocalDateTime start;

    @NotNull(message = "Дата окончания бронирования вещи не может быть пустой")
    @Future(message = "Дата окончания бронирования должна быть в будущем")
    private LocalDateTime end;
}
