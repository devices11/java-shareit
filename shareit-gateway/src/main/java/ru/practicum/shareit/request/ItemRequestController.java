package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Validated @RequestBody ItemRequestCreateDto itemRequestCreateDto) {
        log.info("Получен запрос на вещ от пользователя с ID: {}", userId);
        log.info("Данные запроса: {}", itemRequestCreateDto);

        ResponseEntity<Object> createdItemRequest = itemRequestClient.create(userId, itemRequestCreateDto);

        log.info("Запрос успешно создан: {}", createdItemRequest);
        return createdItemRequest;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllWithoutUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на поиск запросов вещей");

        ResponseEntity<Object> foundItemsRequests = itemRequestClient.findAllWithoutUser(userId);

        log.info("Запросы успешно найдены: {}", foundItemsRequests);
        return foundItemsRequests;
    }

    @GetMapping
    public ResponseEntity<Object> findAllForUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на поиск запросов вещей от пользователя с ID: {}", userId);

        ResponseEntity<Object> foundItemsRequests = itemRequestClient.findAllForUser(userId);

        log.info("Запросы успешно найдены: {}", foundItemsRequests);
        return foundItemsRequests;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@PathVariable long requestId) {
        log.info("Получен запрос на поиск запроса вещей с ID: {}", requestId);

        ResponseEntity<Object> foundItemRequests = itemRequestClient.findById(requestId);

        log.info("Запрос успешно найден: {}", foundItemRequests);
        return foundItemRequests;
    }
}
