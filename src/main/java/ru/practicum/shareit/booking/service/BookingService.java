package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.Collection;

public interface BookingService {

    BookingResponseDto findById(Long userId, Long bookingId);

    Collection<BookingResponseDto> findAllByUserId(Long bookerId, State state);

    Collection<BookingResponseDto> findAllByOwnerId(Long ownerId, State state);

    BookingResponseDto create(Long bookerId, BookingRequestDto bookingRequestDto);

    BookingResponseDto updateStatus(Long ownerId, Long bookingId, boolean approved);

}
