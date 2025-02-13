package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item findById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Вещ с id = {} не найдена", id);
                    return new NotFoundException("Вещ с id " + id + " не найдена");
                });
    }

    @Override
    public Collection<Item> findAllForUser(Long ownerId) {
        checkUser(ownerId);
        return itemRepository.findAllForUser(ownerId);
    }

    @Override
    public Collection<Item> findByText(String text) {
        return itemRepository.findByText(text);
    }

    @Override
    public Item createItem(Item item) {
        checkUser(item.getOwner());
        return itemRepository.add(item);
    }

    @Override
    public Item updateItem(Item item) {
        checkUser(item.getOwner());
        Item itemFromDB = findById(item.getId());
        Item updatedItem = itemFromDB.toBuilder()
                .name(item.getName() != null ? item.getName() : itemFromDB.getName())
                .description(item.getDescription() != null ? item.getDescription() : itemFromDB.getDescription())
                .available(item.getAvailable() != null ? item.getAvailable() : itemFromDB.getAvailable())
                .build();
        return itemRepository.update(updatedItem);
    }

    private void checkUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.info("Пользователь с id = {} не найден", userId);
                    return new NotFoundException("Пользователь с id " + userId + " не найден");
                });
    }
}
