package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@DataJpaTest
public class ItemRepositoryTests {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User owner;
    private Item item;
    private Item item2;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .name("John Wick")
                .email("johnwick@test.com")
                .build();
        User owner2 = User.builder()
                .name("John Wick 2")
                .email("johnwick2@test.com")
                .build();
        userRepository.save(owner);
        userRepository.save(owner2);

        item = Item.builder()
                .name("Test Item")
                .description("Test description")
                .available(true)
                .owner(owner)
                .requestId(1L)
                .build();
        item2 = Item.builder()
                .name("Test Item 2")
                .description("Test description 2")
                .available(true)
                .owner(owner2)
                .requestId(2L)
                .build();
        itemRepository.save(item);
        itemRepository.save(item2);

    }

    @Test
    void findAllByOwner_Id() {
        Collection<Item> foundItems = itemRepository.findAllByOwner_Id(owner.getId());

        assertFalse(foundItems.isEmpty());
        assertEquals(1, foundItems.size());
        assertTrue(foundItems.contains(item));
    }

    @Test
    void findAllByRequestIdIn() {
        Collection<Item> foundItems = itemRepository.findAllByRequestIdIn(List.of(1L));

        assertFalse(foundItems.isEmpty());
        assertEquals(1, foundItems.size());
        assertTrue(foundItems.contains(item));
    }

    @Test
    void findAllByRequestId() {
        Collection<Item> foundItems = itemRepository.findAllByRequestId(2L);

        assertFalse(foundItems.isEmpty());
        assertEquals(1, foundItems.size());
        assertTrue(foundItems.contains(item2));
    }

    @Test
    void searchAvailableItemsIgnoreCase() {
        Collection<Item> foundItems = itemRepository.searchAvailableItemsIgnoreCase("description 2");

        assertFalse(foundItems.isEmpty());
        assertEquals(1, foundItems.size());
        assertTrue(foundItems.contains(item2));
    }
}
