package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class ItemRequestServiceTests {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User user;
    private User owner;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("John2 Wick")
                .email("johnWick@test.com")
                .build();
        userRepository.save(user);

        owner = User.builder()
                .name("John2 Snow")
                .email("johnsnow@test.com")
                .build();
        userRepository.save(owner);

        itemRequest = ItemRequest.builder()
                .description("Test description 3")
                .requestor(user)
                .created(LocalDateTime.of(2025, 3, 11, 12, 0, 0))
                .build();
        itemRequestRepository.save(itemRequest);

        ItemRequest itemRequest2 = ItemRequest.builder()
                .description("Test description 4")
                .requestor(owner)
                .created(LocalDateTime.of(2025, 3, 11, 12, 0, 0))
                .build();
        itemRequestRepository.save(itemRequest2);
    }

    @Test
    void create_shouldReturnCreatedItemRequest() {
        ItemRequestCreateDto request = ItemRequestCreateDto.builder()
                .description("Test description")
                .build();
        ItemRequestResponseDto result = itemRequestService.create(user.getId(), request);

        assertNotNull(result);
        assertEquals(request.getDescription(), result.getDescription());
        assertNotNull(result.getCreated());
        assertNull(result.getItems());
    }

    @Test
    void findAllWithoutUser_shouldReturnItemsRequests() {
        Collection<ItemRequestResponseDto> result = itemRequestService.findAllWithoutUser(user.getId());

        assertEquals(1, result.size());
        assertEquals("Test description 4", result.iterator().next().getDescription());
        assertNotNull(result.iterator().next().getCreated());
        assertNull(result.iterator().next().getItems());
    }

    @Test
    void findAllForUser_shouldReturnItemsRequests() {
        Collection<ItemRequestResponseDto> result = itemRequestService.findAllForUser(owner.getId());

        assertEquals(1, result.size());
        assertEquals("Test description 4", result.iterator().next().getDescription());
        assertNotNull(result.iterator().next().getCreated());
        assertEquals(0, result.iterator().next().getItems().size());
    }

    @Test
    void findById_shouldReturnItemRequest() {
        ItemRequestResponseDto result = itemRequestService.findById(itemRequest.getId());

        assertNotNull(result);
        assertEquals("Test description 3", result.getDescription());
        assertNotNull(result.getCreated());
    }

    @Test
    void findById_shouldThrowNotFoundException_whenRequestNotExists() {
        assertThrows(NotFoundException.class, () -> itemRequestService.findById(999L));
    }
}
