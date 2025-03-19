package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.Collection;

public interface ItemRequestService {

    ItemRequestResponseDto create(Long userId, ItemRequestCreateDto itemRequestCreateDto);

    Collection<ItemRequestResponseDto> findAllWithoutUser(Long userId);

    Collection<ItemRequestResponseDto> findAllForUser(Long userId);

    ItemRequestResponseDto findById(Long requestId);
}
