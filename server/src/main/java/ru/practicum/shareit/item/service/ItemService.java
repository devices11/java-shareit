package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.Collection;

public interface ItemService {

    ItemResponseDto findById(Long id);

    Collection<ItemOwnerResponseDto> findAllForOwner(Long ownerId);

    Collection<ItemResponseDto> findByText(String text);

    ItemResponseDto create(Long ownerId, ItemCreateRequestDto itemCreateRequestDto);

    ItemResponseDto update(Long ownerId, Long itemId, ItemUpdateRequestDto itemUpdateRequestDto);

    CommentResponseDto createComment(Long userId, Long itemId, CommentRequestDto commentDto);
}
