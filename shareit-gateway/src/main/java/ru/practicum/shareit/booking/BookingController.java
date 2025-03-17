package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.State;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable Long bookingId) {
        log.info("Получен запрос на поиск бронирования с ID: {}", bookingId);

        ResponseEntity<Object> foundBooking = bookingClient.findById(userId, bookingId);

        log.info("Бронирование успешно найдено: {}", foundBooking);
        return foundBooking;
    }

    @GetMapping
    public ResponseEntity<Object> findAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(defaultValue = "ALL",
                                                          required = false) String state) {
        log.info("Получен запрос на поиск бронирований для пользователя ID: {}", userId);

        State stateEnum = State.fromString(state);
        ResponseEntity<Object> foundBookings = bookingClient.findAllByUserId(userId, stateEnum);

        log.info("Бронирования успешно найдены: {}", foundBookings);
        return foundBookings;
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                   @RequestParam(defaultValue = "ALL",
                                                           required = false) String state) {
        log.info("Получен запрос на поиск бронирований для владельца вещей с ID: {}", ownerId);

        State stateEnum = State.fromString(state);
        ResponseEntity<Object> foundBookings = bookingClient.findAllByOwnerId(ownerId, stateEnum);

        log.info("Бронирования успешно найдены: {}", foundBookings);
        return foundBookings;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                         @Validated @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Получен запрос на бронирование вещи от пользователя с ID: {}", bookerId);
        log.info("Данные бронирования: {}", bookingRequestDto);

        ResponseEntity<Object> bookedItem = bookingClient.create(bookerId, bookingRequestDto);

        log.info("Создан запрос на бронирование вещи: {}", bookedItem);
        return bookedItem;
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                               @PathVariable Long bookingId,
                                               @RequestParam Boolean approved) {
        log.info("Получен запрос на подтверждение бронирование вещи от пользователя с ID: {}", ownerId);

        ResponseEntity<Object> bookedItem = bookingClient.updateStatus(ownerId, bookingId, approved);

        log.info("Статус бронирования обновлен");
        return bookedItem;
    }
}
