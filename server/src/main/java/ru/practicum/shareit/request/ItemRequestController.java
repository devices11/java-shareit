package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestResponseDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Validated @RequestBody ItemRequestCreateDto itemRequestCreateDto) {
        log.info("Получен запрос на вещ от пользователя с ID: {}", userId);
        log.info("Данные запроса: {}", itemRequestCreateDto);

        ItemRequestResponseDto createdItemRequest = itemRequestService.create(userId, itemRequestCreateDto);

        log.info("Запрос успешно создан: {}", createdItemRequest);
        return createdItemRequest;
    }

    @GetMapping("/all")
    public Collection<ItemRequestResponseDto> findAllWithoutUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на поиск запросов вещей");

        Collection<ItemRequestResponseDto> foundItemsRequests = itemRequestService.findAllWithoutUser(userId);

        log.info("Запросы успешно найдены: {}", foundItemsRequests);
        return foundItemsRequests;
    }

    @GetMapping
    public Collection<ItemRequestResponseDto> findAllForUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на поиск запросов вещей от пользователя с ID: {}", userId);

        Collection<ItemRequestResponseDto> foundItemsRequests = itemRequestService.findAllForUser(userId);

        log.info("Запросы успешно найдены: {}", foundItemsRequests);
        return foundItemsRequests;
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto findById(@PathVariable long requestId) {
        log.info("Получен запрос на поиск запроса вещей с ID: {}", requestId);

        ItemRequestResponseDto foundItemRequests = itemRequestService.findById(requestId);

        log.info("Запрос успешно найден: {}", foundItemRequests);
        return foundItemRequests;
    }
}
