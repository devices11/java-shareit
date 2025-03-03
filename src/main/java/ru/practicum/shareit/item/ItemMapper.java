package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingForItemResponseDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "available", source = "available")
    ItemResponseDto toItemResponseDto(Item item);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "description", source = "item.description")
    @Mapping(target = "available", source = "item.available")
    @Mapping(target = "comments", source = "comments")
    ItemResponseDto toItemWithCommentsResponseDto(Item item, Collection<CommentResponseDto> comments);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "description", source = "item.description")
    @Mapping(target = "available", source = "item.available")
    @Mapping(target = "lastBooking", source = "lastBooking")
    @Mapping(target = "nextBooking", source = "nextBooking")
    ItemOwnerResponseDto toItemOwnerResponseDto(Item item, BookingForItemResponseDto lastBooking,
                                                BookingForItemResponseDto nextBooking);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "itemDto.name")
    @Mapping(target = "description", source = "itemDto.description")
    @Mapping(target = "available", source = "itemDto.available")
    @Mapping(target = "owner", source = "owner")
    Item toItem(ItemCreateRequestDto itemDto, User owner);

    @Mapping(target = "id", source = "itemId")
    @Mapping(target = "name", source = "itemDto.name")
    @Mapping(target = "description", source = "itemDto.description")
    @Mapping(target = "available", source = "itemDto.available")
    @Mapping(target = "owner", source = "owner")
    Item toItem(ItemUpdateRequestDto itemDto, Long itemId, User owner);
}
