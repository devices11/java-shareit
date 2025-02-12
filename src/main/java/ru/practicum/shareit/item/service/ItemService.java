package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    Item findById(Long id);

    Collection<Item> findAllForUser(Long ownerId);

    Collection<Item> findByText(String text);

    Item createItem(Item item);

    Item updateItem(Item item);

}
