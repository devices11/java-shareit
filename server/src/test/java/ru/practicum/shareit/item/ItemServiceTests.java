package ru.practicum.shareit.item;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingForItemResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class ItemServiceTests {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    private CommentResponseDto commentsResponseDto;
    private User owner;
    private User owner2;
    private Item item;
    private BookingForItemResponseDto lastBooking;
    private BookingForItemResponseDto nextBooking;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .name("John Snow")
                .email("johnsnow@test.com")
                .build();
        userRepository.save(owner);

        owner2 = User.builder()
                .name("John Wick")
                .email("johnwick@test.com")
                .build();
        userRepository.save(owner2);
        UserResponseDto booker = UserResponseDto.builder()
                .id(owner2.getId())
                .name(owner2.getName())
                .email(owner2.getEmail())
                .build();

        item = Item.builder()
                .name("Test Item John Snow")
                .description("Test description")
                .available(true)
                .owner(owner)
                .requestId(1L)
                .build();
        itemRepository.save(item);

        Item item2 = Item.builder()
                .name("Test Item 2 John Wick")
                .description("Test description 2")
                .available(true)
                .owner(owner2)
                .build();
        itemRepository.save(item2);

        Comment comment = Comment.builder()
                .text("This is a comment")
                .item(item)
                .author(owner)
                .created(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
        commentsResponseDto = CommentResponseDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();

        Booking booking = Booking.builder()
                .start(LocalDateTime.of(2025, 3, 11, 12, 0, 0))
                .end(LocalDateTime.of(2025, 3, 12, 12, 0, 0))
                .item(item)
                .booker(owner2)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.of(2025, 4, 11, 12, 0, 0))
                .end(LocalDateTime.of(2025, 4, 12, 12, 0, 0))
                .item(item)
                .booker(owner2)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking2);

        lastBooking = BookingForItemResponseDto.builder()
                .start(LocalDateTime.of(2025, 3, 11, 12, 0, 0))
                .end(LocalDateTime.of(2025, 3, 12, 12, 0, 0))
                .status(BookingStatus.APPROVED)
                .booker(booker)
                .build();
        nextBooking = BookingForItemResponseDto.builder()
                .start(LocalDateTime.of(2025, 4, 11, 12, 0, 0))
                .end(LocalDateTime.of(2025, 4, 12, 12, 0, 0))
                .status(BookingStatus.APPROVED)
                .booker(booker)
                .build();
    }

    @Test
    void findById_shouldReturnItemResponseDto() {
        ItemResponseDto expectedResult = ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(true)
                .lastBooking(null)
                .nextBooking(null)
                .comments(List.of(commentsResponseDto))
                .requestId(1L)
                .build();

        ItemResponseDto result = itemService.findById(item.getId());

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    void findAllForOwner_shouldReturnItemOwnerResponseDto() {
        ItemOwnerResponseDto expectedResult = ItemOwnerResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(true)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();

        Collection<ItemOwnerResponseDto> result = itemService.findAllForOwner(owner.getId());

        assertNotNull(result);
        assertEquals(List.of(expectedResult), result);
    }

    @Test
    void findByText_shouldReturnItemResponseDto() {
        ItemResponseDto expectedResult = ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(true)
                .lastBooking(null)
                .nextBooking(null)
                .requestId(1L)
                .build();

        Collection<ItemResponseDto> result = itemService.findByText("Item John");

        assertEquals(1, result.size());
        assertEquals(expectedResult, result.iterator().next());
    }

    @Test
    void create_shouldReturnItemResponseDto() {
        ItemCreateRequestDto itemCreateRequestDto = ItemCreateRequestDto.builder()
                .name("Test Item John Snow 2")
                .description("Test description 2")
                .available(true)
                .requestId(2L)
                .build();

        ItemResponseDto result = itemService.create(owner.getId(), itemCreateRequestDto);

        assertNotNull(result);
        ItemResponseDto expectedResult = ItemResponseDto.builder()
                .id(result.getId())
                .name(itemCreateRequestDto.getName())
                .description(itemCreateRequestDto.getDescription())
                .available(true)
                .lastBooking(null)
                .nextBooking(null)
                .requestId(2L)
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void update_shouldReturnItemResponseDto() {
        ItemUpdateRequestDto itemUpdateRequestDto = ItemUpdateRequestDto.builder()
                .name("Test Item John Snow 3")
                .description("Test description 3")
                .available(false)
                .build();

        ItemResponseDto result = itemService.update(owner.getId(), item.getId(), itemUpdateRequestDto);

        assertNotNull(result);
        ItemResponseDto expectedResult = ItemResponseDto.builder()
                .id(item.getId())
                .name(itemUpdateRequestDto.getName())
                .description(itemUpdateRequestDto.getDescription())
                .available(itemUpdateRequestDto.getAvailable())
                .lastBooking(null)
                .nextBooking(null)
                .requestId(1L)
                .build();
        assertEquals(expectedResult, result);
    }

    @Test
    void createComment_shouldReturnCommentResponseDto() {
        CommentRequestDto itemCreateRequestDto = CommentRequestDto.builder()
                .text("Test comment")
                .build();

        CommentResponseDto result = itemService.createComment(owner2.getId(), item.getId(), itemCreateRequestDto);

        assertNotNull(result);
        CommentResponseDto expectedResult = CommentResponseDto.builder()
                .id(result.getId())
                .text(itemCreateRequestDto.getText())
                .authorName(owner2.getName())
                .created(result.getCreated())
                .build();
        assertEquals(expectedResult, result);
    }
}
