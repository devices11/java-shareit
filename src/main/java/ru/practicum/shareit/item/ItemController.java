package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{id}")
    public ItemResponseDto findById(@PathVariable long id) {
        log.info("Получен запрос на поиск вещи с ID: {}", id);

        ItemResponseDto foundItem = itemService.findById(id);

        log.info("Вещь успешно найдена: {}", foundItem);
        return foundItem;
    }

    @GetMapping
    public Collection<ItemOwnerResponseDto> findAllForUser(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос на поиск вещей пользователя с ID: {}", ownerId);

        Collection<ItemOwnerResponseDto> foundItems = itemService.findAllForOwner(ownerId);

        log.info("Вещи успешно найдены: {}", foundItems);
        return foundItems;
    }

    @GetMapping("/search")
    public Collection<ItemResponseDto> findByText(@RequestParam(name = "text", required = false) String text) {
        log.info("Получен запрос на поиск вещей по тексту: {}", text);

        Collection<ItemResponseDto> foundItems = itemService.findByText(text);

        log.info("Вещи успешно найдены: {}", foundItems);
        return foundItems;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponseDto create(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                  @Validated @RequestBody ItemCreateRequestDto itemCreateRequestDto) {
        log.info("Получен запрос на добавление новой вещи от пользователя с ID: {}", ownerId);
        log.info("Данные вещи: {}", itemCreateRequestDto);

        ItemResponseDto createdItem = itemService.create(ownerId, itemCreateRequestDto);

        log.info("Вещь успешно создана: {}", createdItem);
        return createdItem;
    }

    @PatchMapping("/{id}")
    public ItemResponseDto update(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                  @PathVariable Long id,
                                  @Validated @RequestBody ItemUpdateRequestDto itemUpdateRequestDto) {
        log.info("Получен запрос на обновление вещи от пользователя с ID: {}", ownerId);
        log.info("Данные вещи: {}", itemUpdateRequestDto);

        ItemResponseDto updatedItem = itemService.update(ownerId, id, itemUpdateRequestDto);

        log.info("Вещь успешно обновлена: {}", updatedItem);
        return updatedItem;
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @PathVariable Long itemId,
                                            @Validated @RequestBody CommentRequestDto commentDto) {
        log.info("Получен запрос на добавление комментария к вещи ID: {}", itemId);

        CommentResponseDto createdComment = itemService.createComment(userId, itemId, commentDto);

        log.info("Комментарий успешно создан: {}", createdComment);
        return createdComment;
    }
}
