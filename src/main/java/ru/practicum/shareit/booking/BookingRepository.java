package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBooker_IdOrderByStartDesc(long bookerId);

    List<Booking> findByBooker_IdAndStatusAndStartBeforeAndEndAfterOrderByStart(long bookerId,
                                                                                BookingStatus status,
                                                                                LocalDateTime start,
                                                                                LocalDateTime end);

    List<Booking> findByBooker_IdAndStartAfterOrderByStart(long bookerId, LocalDateTime start);

    List<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime end);

    Boolean existsByBooker_IdAndItem_IdAndStatusAndEndBefore(long userId, long itemId, BookingStatus status,
                                                             LocalDateTime end);

    List<Booking> findByBooker_IdAndStatusOrderByStartDesc(long bookerId, BookingStatus status);

    List<Booking> findByItem_Owner_IdOrderByStartDesc(Long ownerId);

    List<Booking> findByItem_Owner_IdAndStatusAndStartBeforeAndEndAfterOrderByStart(long ownerId,
                                                                                    BookingStatus status,
                                                                                    LocalDateTime start,
                                                                                    LocalDateTime end);

    List<Booking> findByItem_Owner_IdAndStartAfterOrderByStart(long ownerId, LocalDateTime start);

    List<Booking> findByItem_Owner_IdAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime end);

    List<Booking> findByItem_Owner_IdAndStatusOrderByStartDesc(long ownerId, BookingStatus status);
}
