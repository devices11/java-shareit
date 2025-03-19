package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemForRequestorResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemMapper itemMapper;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    @Transactional
    public ItemRequestResponseDto create(Long userId, ItemRequestCreateDto itemRequestCreateDto) {
        User user = findUserById(userId);
        ItemRequest request = itemRequestMapper.toItemRequest(user, itemRequestCreateDto);
        ItemRequest requestCreated = itemRequestRepository.save(request);
        return itemRequestMapper.toItemRequestResponseDto(requestCreated);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestResponseDto> findAllWithoutUser(Long userId) {
        findUserById(userId);
        Collection<ItemRequest> itemRequestsFromDb = itemRequestRepository.findAllByRequestorIdIsNot(userId);
        return itemRequestsFromDb.stream()
                .map(itemRequestMapper::toItemRequestResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestResponseDto> findAllForUser(Long userId) {
        findUserById(userId);
        Collection<ItemRequest> requestFromDB = itemRequestRepository.findAllByRequestor_IdOrderByCreated(userId);
        Collection<Long> requestsIds = requestFromDB.stream()
                .map(ItemRequest::getId)
                .toList();
        Collection<Item> itemsFromDB = itemRepository.findAllByRequestIdIn(requestsIds);
        Map<Long, List<Item>> requestedItems = itemsFromDB.stream()
                .collect(Collectors.groupingBy(Item::getRequestId));

        return requestFromDB.stream()
                .map(itemRequest -> {
                    Collection<Item> items = requestedItems.getOrDefault(itemRequest.getId(), Collections.emptyList());
                    Collection<ItemForRequestorResponseDto> itemsDto = items.stream()
                            .map(itemMapper::toItemForRequestorResponseDto)
                            .toList();
                    return itemRequestMapper.toItemRequestResponseDto(itemRequest, itemsDto);

                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestResponseDto findById(Long requestId) {
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос вещи с id " + requestId + " не найден"));

        Collection<Item> itemsFromDB = itemRepository.findAllByRequestId(requestId);
        Collection<ItemForRequestorResponseDto> itemsDto = itemsFromDB.stream()
                .map(itemMapper::toItemForRequestorResponseDto)
                .toList();

        return itemRequestMapper.toItemRequestResponseDto(request, itemsDto);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }
}
