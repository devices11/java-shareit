package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.util.validation.groups.Create;
import ru.practicum.shareit.util.validation.groups.Update;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{id}")
    public ItemDto findById(@PathVariable long id) {
        log.info("Получен запрос на поиск вещи с ID: {}", id);

        Item foundItem = itemService.findById(id);
        ItemDto foundItemsDto = ItemMapper.toItemDto(foundItem);

        log.info("Вещь успешно найдена: {}", foundItemsDto);
        return foundItemsDto;
    }

    @GetMapping
    public Collection<ItemDto> findAllForUser(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос на поиск вещей пользователя с ID: {}", ownerId);

        Collection<Item> foundItems = itemService.findAllForUser(ownerId);
        Collection<ItemDto> foundItemsDto = foundItems.stream()
                .map(ItemMapper::toItemDto)
                .toList();

        log.info("Вещи успешно найдены: {}", foundItemsDto);
        return foundItemsDto;
    }

    @GetMapping("/search")
    public Collection<ItemDto> findByText(@RequestParam(name = "text", required = false) String text) {
        log.info("Получен запрос на поиск вещей по тексту: {}", text);

        Collection<Item> foundItems = itemService.findByText(text);
        Collection<ItemDto> foundItemsDto = foundItems.stream()
                .map(ItemMapper::toItemDto)
                .toList();

        log.info("Вещи успешно найдены: {}", foundItemsDto);
        return foundItemsDto;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                          @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на добавление новой вещи от пользователя с ID: {}", ownerId);
        log.info("Данные вещи: {}", itemDto);

        Item item = ItemMapper.toItem(itemDto, ownerId);
        Item createdItem = itemService.createItem(item);
        ItemDto createdItemDto = ItemMapper.toItemDto(createdItem);

        log.info("Вещь успешно создана: {}", createdItemDto);
        return createdItemDto;
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                          @PathVariable Long id,
                          @Validated(Update.class) @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на обновление вещи от пользователя с ID: {}", ownerId);
        log.info("Данные вещи: {}", itemDto);

        Item item = ItemMapper.toItem(itemDto, ownerId);
        Item updatedItem = itemService.updateItem(item.toBuilder().id(id).build());
        ItemDto updatedItemDto = ItemMapper.toItemDto(updatedItem);

        log.info("Вещь успешно обновлена: {}", updatedItemDto);
        return updatedItemDto;
    }
}
