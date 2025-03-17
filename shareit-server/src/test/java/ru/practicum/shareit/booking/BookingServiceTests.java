package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class BookingServiceTests {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private User user2;
    private Item item;
    private Booking booking;
    private Booking booking2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("John Snow")
                .email("johnsnow@test.com")
                .build();
        userRepository.save(user);

        user2 = User.builder()
                .name("John Wick")
                .email("johnwick@test.com")
                .build();
        userRepository.save(user2);

        item = Item.builder()
                .name("Test Item John Snow")
                .description("Test description")
                .available(true)
                .owner(user)
                .requestId(1L)
                .build();
        itemRepository.save(item);

        Item item2 = Item.builder()
                .name("Test Item 2 John Wick")
                .description("Test description 2")
                .available(true)
                .owner(user2)
                .build();
        itemRepository.save(item2);

        booking = Booking.builder()
                .start(LocalDateTime.of(2025, 3, 11, 12, 0, 0))
                .end(LocalDateTime.of(2025, 3, 12, 12, 0, 0))
                .item(item)
                .booker(user2)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        booking2 = Booking.builder()
                .start(LocalDateTime.of(2025, 4, 11, 12, 0, 0))
                .end(LocalDateTime.of(2025, 4, 12, 12, 0, 0))
                .item(item2)
                .booker(user2)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking2);
    }

    @Test
    void findById_shouldReturnBookingResponseDto() {
        BookingResponseDto result = bookingService.findById(user2.getId(), booking.getId());

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
        assertEquals(booking.getStatus(), result.getStatus());
        assertEquals(UserMapper.INSTANCE.toUserResponseDto(booking.getBooker()), result.getBooker());
        assertEquals(ItemMapper.INSTANCE.toItemResponseDto(booking.getItem()), result.getItem());
    }

    @Test
    void findAllByUserId_StateALL_shouldReturnBookingResponseDto() {
        Collection<BookingResponseDto> result = bookingService.findAllByUserId(user2.getId(), State.ALL);

        assertEquals(2, result.size());
        assertEquals(booking2.getId(), result.iterator().next().getId());
        assertEquals(booking2.getStart(), result.iterator().next().getStart());
        assertEquals(booking2.getEnd(), result.iterator().next().getEnd());
        assertEquals(booking2.getStatus(), result.iterator().next().getStatus());
        assertEquals(UserMapper.INSTANCE.toUserResponseDto(booking2.getBooker()), result.iterator().next().getBooker());
        assertEquals(ItemMapper.INSTANCE.toItemResponseDto(booking2.getItem()), result.iterator().next().getItem());
    }

    @Test
    void findAllByUserId_StateFUTURE_shouldReturnBookingResponseDto() {
        Collection<BookingResponseDto> result = bookingService.findAllByUserId(user2.getId(), State.FUTURE);

        assertEquals(1, result.size());
        assertEquals(booking2.getId(), result.iterator().next().getId());
        assertEquals(booking2.getStart(), result.iterator().next().getStart());
        assertEquals(booking2.getEnd(), result.iterator().next().getEnd());
        assertEquals(booking2.getStatus(), result.iterator().next().getStatus());
        assertEquals(UserMapper.INSTANCE.toUserResponseDto(booking2.getBooker()), result.iterator().next().getBooker());
        assertEquals(ItemMapper.INSTANCE.toItemResponseDto(booking2.getItem()), result.iterator().next().getItem());
    }

    @Test
    void findAllByOwnerId_StateALL_shouldReturnBookingResponseDto() {
        Collection<BookingResponseDto> result = bookingService.findAllByOwnerId(user.getId(), State.ALL);

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.iterator().next().getId());
        assertEquals(booking.getStart(), result.iterator().next().getStart());
        assertEquals(booking.getEnd(), result.iterator().next().getEnd());
        assertEquals(booking.getStatus(), result.iterator().next().getStatus());
        assertEquals(UserMapper.INSTANCE.toUserResponseDto(booking.getBooker()), result.iterator().next().getBooker());
        assertEquals(ItemMapper.INSTANCE.toItemResponseDto(booking.getItem()), result.iterator().next().getItem());
    }

    @Test
    void findAllByOwnerId_StateFUTURE_shouldReturnBookingResponseDto() {
        Collection<BookingResponseDto> result = bookingService.findAllByOwnerId(user.getId(), State.FUTURE);

        assertEquals(0, result.size());
    }

    @Test
    void create_shouldReturnBookingResponseDto() {
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.of(2025, 5, 11, 12, 0, 0))
                .end(LocalDateTime.of(2025, 5, 12, 12, 0, 0))
                .build();

        BookingResponseDto result = bookingService.create(user2.getId(), bookingRequestDto);

        assertNotNull(result);
        assertEquals(bookingRequestDto.getStart(), result.getStart());
        assertEquals(bookingRequestDto.getEnd(), result.getEnd());
        assertEquals(BookingStatus.WAITING, result.getStatus());
        assertEquals(UserMapper.INSTANCE.toUserResponseDto(booking.getBooker()), result.getBooker());
        assertEquals(ItemMapper.INSTANCE.toItemResponseDto(booking.getItem()), result.getItem());
    }

    @Test
    void create_shouldReturnValidationException() {
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.of(2025, 3, 11, 12, 0, 0))
                .end(LocalDateTime.of(2025, 3, 12, 12, 0, 0))
                .build();

        assertThrows(ValidationException.class, () -> bookingService.create(user2.getId(), bookingRequestDto));
    }

    @Test
    void updateStatus_shouldReturnBookingResponseDto_WithBookingStatusAPPROVED() {
        Booking booking3 = Booking.builder()
                .start(LocalDateTime.of(2025, 3, 14, 12, 0, 0))
                .end(LocalDateTime.of(2025, 3, 15, 12, 0, 0))
                .item(item)
                .booker(user2)
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking3);

        BookingResponseDto result = bookingService.updateStatus(user.getId(), booking3.getId(), true);

        assertNotNull(result);
        assertEquals(booking3.getStart(), result.getStart());
        assertEquals(booking3.getEnd(), result.getEnd());
        assertEquals(BookingStatus.APPROVED, result.getStatus());
        assertEquals(UserMapper.INSTANCE.toUserResponseDto(booking.getBooker()), result.getBooker());
        assertEquals(ItemMapper.INSTANCE.toItemResponseDto(booking.getItem()), result.getItem());
    }

    @Test
    void updateStatus_shouldReturnBookingResponseDto_WithBookingStatusREJECTED() {
        Booking booking3 = Booking.builder()
                .start(LocalDateTime.of(2025, 3, 14, 12, 0, 0))
                .end(LocalDateTime.of(2025, 3, 15, 12, 0, 0))
                .item(item)
                .booker(user2)
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking3);

        BookingResponseDto result = bookingService.updateStatus(user.getId(), booking3.getId(), false);

        assertNotNull(result);
        assertEquals(booking3.getStart(), result.getStart());
        assertEquals(booking3.getEnd(), result.getEnd());
        assertEquals(BookingStatus.REJECTED, result.getStatus());
        assertEquals(UserMapper.INSTANCE.toUserResponseDto(booking.getBooker()), result.getBooker());
        assertEquals(ItemMapper.INSTANCE.toItemResponseDto(booking.getItem()), result.getItem());
    }
}
