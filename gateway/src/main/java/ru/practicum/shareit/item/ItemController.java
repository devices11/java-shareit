package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable long id) {
        log.info("Получен запрос на поиск вещи с ID: {}", id);

        ResponseEntity<Object> foundItem = itemClient.findById(id);

        log.info("Вещь успешно найдена: {}", foundItem);
        return foundItem;
    }

    @GetMapping
    public ResponseEntity<Object> findAllForUser(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос на поиск вещей пользователя с ID: {}", ownerId);

        ResponseEntity<Object> foundItems = itemClient.findAllForOwner(ownerId);

        log.info("Вещи успешно найдены: {}", foundItems);
        return foundItems;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findByText(@RequestParam(name = "text", required = false) String text) {
        log.info("Получен запрос на поиск вещей по тексту: {}", text);

        ResponseEntity<Object> foundItems = itemClient.findByText(text);

        log.info("Вещи успешно найдены: {}", foundItems);
        return foundItems;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                         @Validated @RequestBody ItemCreateRequestDto itemCreateRequestDto) {
        log.info("Получен запрос на добавление новой вещи от пользователя с ID: {}", ownerId);
        log.info("Данные вещи: {}", itemCreateRequestDto);

        ResponseEntity<Object> createdItem = itemClient.create(ownerId, itemCreateRequestDto);

        log.info("Вещь успешно создана: {}", createdItem);
        return createdItem;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                         @PathVariable Long id,
                                         @Validated @RequestBody ItemUpdateRequestDto itemUpdateRequestDto) {
        log.info("Получен запрос на обновление вещи от пользователя с ID: {}", ownerId);
        log.info("Данные вещи: {}", itemUpdateRequestDto);

        ResponseEntity<Object> updatedItem = itemClient.update(ownerId, id, itemUpdateRequestDto);

        log.info("Вещь успешно обновлена: {}", updatedItem);
        return updatedItem;
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long itemId,
                                                @Validated @RequestBody CommentRequestDto commentDto) {
        log.info("Получен запрос на добавление комментария к вещи ID: {}", itemId);

        ResponseEntity<Object> createdComment = itemClient.createComment(userId, itemId, commentDto);

        log.info("Комментарий успешно создан: {}", createdComment);
        return createdComment;
    }
}
