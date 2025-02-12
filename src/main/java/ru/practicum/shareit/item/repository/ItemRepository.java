package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {

    Optional<Item> findById(long id);

    Collection<Item> findAllForUser(long userId);

    Collection<Item> findByText(String text);

    Item add(Item item);

    Item update(Item item);


}
