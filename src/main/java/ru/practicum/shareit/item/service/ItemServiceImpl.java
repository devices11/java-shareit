package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingForItemResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.NotFoundException;
import ru.practicum.shareit.util.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemResponseDto findById(Long itemId) {
        Item itemFromDB = findItemById(itemId);
        Collection<CommentResponseDto> comments = commentRepository.findByItem_Id(itemId).stream()
                .map(commentMapper::toCommentResponseDto)
                .toList();
        return itemMapper.toItemWithCommentsResponseDto(itemFromDB, comments);
    }

    @Override
    public Collection<ItemOwnerResponseDto> findAllForOwner(Long ownerId) {
        findUserById(ownerId);
        Collection<Item> itemsFromDB = itemRepository.findAllByOwner_Id(ownerId);
        Map<Long, List<Booking>> bookings = bookingRepository.findByItem_Owner_IdOrderByStartDesc(ownerId).stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));

        return itemsFromDB.stream()
                .map(item -> {
                    List<Booking> itemBookings = bookings.getOrDefault(item.getId(), Collections.emptyList());

                    Booking nextBooking = itemBookings.stream()
                            .filter(booking -> booking.getStart().isAfter(LocalDateTime.now())
                                    && booking.getStatus().equals(BookingStatus.APPROVED))
                            .findFirst()
                            .orElse(null);

                    Booking lastBooking = itemBookings.stream()
                            .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now())
                                    && booking.getStatus().equals(BookingStatus.APPROVED))
                            .findFirst()
                            .orElse(null);

                    BookingForItemResponseDto nextBookingDto = nextBooking != null
                            ? bookingMapper.bookingForItemResponseDto(nextBooking)
                            : null;
                    BookingForItemResponseDto lastBookingDto = lastBooking != null
                            ? bookingMapper.bookingForItemResponseDto(lastBooking)
                            : null;

                    return itemMapper.toItemOwnerResponseDto(item, lastBookingDto, nextBookingDto);
                })
                .toList();
    }

    @Override
    public Collection<ItemResponseDto> findByText(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        Collection<Item> itemsFromDB = itemRepository
                .searchAvailableItemsIgnoreCase(text);
        return itemsFromDB.stream()
                .map(itemMapper::toItemResponseDto)
                .toList();
    }

    @Override
    public ItemResponseDto create(Long ownerId, ItemCreateRequestDto itemCreateRequestDto) {
        User user = findUserById(ownerId);
        Item item = itemMapper.toItem(itemCreateRequestDto, user);
        Item createdItem = itemRepository.save(item);
        return itemMapper.toItemResponseDto(createdItem);
    }

    @Override
    public ItemResponseDto update(Long ownerId, Long itemId, ItemUpdateRequestDto itemUpdateRequestDto) {
        User user = findUserById(ownerId);
        Item item = itemMapper.toItem(itemUpdateRequestDto, itemId, user);
        Item itemFromDB = findItemById(itemId);
        Item itemForUpdate = itemFromDB.toBuilder()
                .name(item.getName() != null ? item.getName() : itemFromDB.getName())
                .description(item.getDescription() != null ? item.getDescription() : itemFromDB.getDescription())
                .available(item.getAvailable() != null ? item.getAvailable() : itemFromDB.getAvailable())
                .build();
        Item updatedItem = itemRepository.save(itemForUpdate);
        return itemMapper.toItemResponseDto(updatedItem);
    }

    @Override
    public CommentResponseDto createComment(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        User user = findUserById(userId);
        Item itemFromDB = findItemById(itemId);
        if (!hasUserBookedItem(userId, itemId)) {
            throw new ValidationException("Пользователь с ID " + userId + " не бронировал вещь с ID " + itemId);
        }
        Comment comment = commentMapper.toComment(commentRequestDto, itemFromDB, user);
        Comment commentCreated = commentRepository.save(comment);
        return commentMapper.toCommentResponseDto(commentCreated);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }

    private boolean hasUserBookedItem(Long userId, Long itemId) {
        return bookingRepository.existsByBooker_IdAndItem_IdAndStatusAndEndBefore(
                userId, itemId, BookingStatus.APPROVED, LocalDateTime.now());
    }

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));
    }
}
