package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingForItemResponseDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "start", source = "start")
    @Mapping(target = "end", source = "end")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "booker", source = "booker")
    @Mapping(target = "item", source = "item")
    BookingResponseDto toBookingResponseDto(Booking booking);

    @Mapping(target = "start", source = "start")
    @Mapping(target = "end", source = "end")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "booker", source = "booker")
    BookingForItemResponseDto toBookingForItemResponseDto(Booking booking);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "start", source = "bookingRequestDto.start")
    @Mapping(target = "end", source = "bookingRequestDto.end")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "booker", source = "booker")
    @Mapping(target = "status", expression = "java(ru.practicum.shareit.booking.model.BookingStatus.WAITING)")
    Booking toBooking(User booker, Item item, BookingRequestDto bookingRequestDto);
}