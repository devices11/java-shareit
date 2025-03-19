package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@Transactional
@DataJpaTest
public class ItemRequestRepositoryTests {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllByRequestor_IdOrderByCreated_shouldReturnItemsRequests() {
        User user = User.builder()
                .name("John Wick")
                .email("johnwick@test.com")
                .build();
        userRepository.save(user);
        ItemRequest itemRequest = ItemRequest.builder()
                .description("Test description")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();
        itemRequestRepository.save(itemRequest);

        Collection<ItemRequest> foundItemRequest = itemRequestRepository.findAllByRequestor_IdOrderByCreated(user.getId());

        assertTrue(foundItemRequest.contains(itemRequest));
        assertEquals(1, foundItemRequest.size());
        assertEquals(itemRequest, foundItemRequest.iterator().next());
    }

    @Test
    void findAllByRequestorIdIsNot_shouldReturnEmpty() {
        User user2 = User.builder()
                .name("John Wick")
                .email("johnwick@test.com")
                .build();
        userRepository.save(user2);
        ItemRequest itemRequest = ItemRequest.builder()
                .description("Test description")
                .requestor(user2)
                .created(LocalDateTime.now())
                .build();
        itemRequestRepository.save(itemRequest);

        Collection<ItemRequest> foundItemRequest = itemRequestRepository.findAllByRequestorIdIsNot(user2.getId());

        assertTrue(foundItemRequest.isEmpty());
    }
}
