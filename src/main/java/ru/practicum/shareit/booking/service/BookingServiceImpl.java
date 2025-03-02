package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.NotFoundException;
import ru.practicum.shareit.util.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;


    @Override
    public BookingResponseDto findById(Long userId, Long bookingId) {
        Booking bookingFromDB = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));
        if (!userId.equals(bookingFromDB.getBooker().getId())
                && !userId.equals(bookingFromDB.getItem().getOwner().getId())) {
            throw new ValidationException("Бронирование не принадлежит пользователю или владельцу вещи");
        }
        return bookingMapper.bookingResponseDto(bookingFromDB);
    }

    @Override
    public Collection<BookingResponseDto> findAllByUserId(Long bookerId, State state) {
        findUserById(bookerId);
        Collection<Booking> bookingsFromDB = switch (state) {
            case ALL -> bookingRepository.findByBooker_IdOrderByStartDesc(bookerId);
            case CURRENT -> bookingRepository.findByBooker_IdAndStatusAndStartBeforeAndEndAfterOrderByStart(
                    bookerId, BookingStatus.APPROVED, LocalDateTime.now(), LocalDateTime.now());
            case PAST -> bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now());
            case FUTURE -> bookingRepository.findByBooker_IdAndStartAfterOrderByStart(bookerId, LocalDateTime.now());
            case WAITING -> bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING);
            case REJECTED ->
                    bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED);
        };
        return bookingsFromDB.stream()
                .map(bookingMapper::bookingResponseDto)
                .toList();
    }

    @Override
    public Collection<BookingResponseDto> findAllByOwnerId(Long ownerId, State state) {
        findUserById(ownerId);
        Collection<Booking> bookingsFromDB = switch (state) {
            case ALL -> bookingRepository.findByItem_Owner_IdOrderByStartDesc(ownerId);
            case CURRENT -> bookingRepository.findByItem_Owner_IdAndStatusAndStartBeforeAndEndAfterOrderByStart(
                    ownerId, BookingStatus.APPROVED, LocalDateTime.now(), LocalDateTime.now());
            case PAST ->
                    bookingRepository.findByItem_Owner_IdAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now());
            case FUTURE -> bookingRepository.findByItem_Owner_IdAndStartAfterOrderByStart(ownerId, LocalDateTime.now());
            case WAITING ->
                    bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
            case REJECTED ->
                    bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
        };
        return bookingsFromDB.stream()
                .map(bookingMapper::bookingResponseDto)
                .toList();
    }

    @Override
    public BookingResponseDto create(Long bookerId, BookingRequestDto bookingRequestDto) {
        if (bookingRequestDto.getStart().equals(bookingRequestDto.getEnd()))
            throw new ValidationException("Время начала и окончания бронирования не должно совпадать");
        User userFromDB = findUserById(bookerId);
        Item itemFromDB = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id " + bookingRequestDto.getItemId() + " не найдена"));
        if (itemFromDB.getAvailable().equals(false))
            throw new ValidationException("Вещь уже забронирована другим пользователем");
        Booking booking = bookingMapper.toBooking(userFromDB, itemFromDB, bookingRequestDto);

        Booking createdBooking = bookingRepository.save(booking);
        return bookingMapper.bookingResponseDto(createdBooking);
    }

    @Override
    public BookingResponseDto updateStatus(Long ownerId, Long bookingId, boolean approved) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new ValidationException("Пользователь с id " + ownerId + " не найден"));
        Booking bookingFromDB = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));
        if (approved) {
            bookingFromDB.setStatus(BookingStatus.APPROVED);
        } else {
            bookingFromDB.setStatus(BookingStatus.REJECTED);
        }
        Booking updatedBooking = bookingRepository.save(bookingFromDB);
        return bookingMapper.bookingResponseDto(updatedBooking);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }
}
