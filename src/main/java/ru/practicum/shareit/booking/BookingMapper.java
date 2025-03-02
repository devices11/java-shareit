package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingForItemResponseDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    public BookingResponseDto bookingResponseDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(userMapper.toUserResponseDto(booking.getBooker()))
                .item(itemMapper.toItemResponseDto(booking.getItem()))
                .build();
    }

    public BookingForItemResponseDto bookingForItemResponseDto(Booking booking) {
        return BookingForItemResponseDto.builder()
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(userMapper.toUserResponseDto(booking.getBooker()))
                .build();
    }

    public Booking toBooking(User booker, Item item, BookingRequestDto bookingRequestDto) {
        return Booking.builder()
                .start(bookingRequestDto.getStart())
                .end(bookingRequestDto.getEnd())
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
    }
}
