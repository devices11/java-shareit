package ru.practicum.shareit.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemForRequestorResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface ItemRequestMapper {
    ItemRequestMapper INSTANCE = Mappers.getMapper(ItemRequestMapper.class);

    @Mapping(target = "id", source = "itemRequest.id")
    @Mapping(target = "description", source = "itemRequest.description")
    @Mapping(target = "created", source = "itemRequest.created")
    ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest);

    @Mapping(target = "id", source = "itemRequest.id")
    @Mapping(target = "description", source = "itemRequest.description")
    @Mapping(target = "created", source = "itemRequest.created")
    @Mapping(target = "items", source = "itemsForRequestorResponseDto")
    ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest,
                                                    Collection<ItemForRequestorResponseDto> itemsForRequestorResponseDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", source = "itemRequestCreateDto.description")
    @Mapping(target = "requestor", source = "user")
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    ItemRequest toItemRequest(User user, ItemRequestCreateDto itemRequestCreateDto);
}
