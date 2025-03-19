package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@DataJpaTest
public class BookingRepositoryTests {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        booker = User.builder()
                .name("John Wick")
                .email("johnwick@test.com")
                .build();
        userRepository.save(booker);

        item = Item.builder()
                .name("Item")
                .description("Description")
                .available(true)
                .owner(booker)
                .build();
        itemRepository.save(item);
    }

    @Test
    public void findByBooker_IdOrderByStartDesc() {
        Booking booking = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);


        Booking booking2 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now())
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findByBooker_IdOrderByStartDesc(booker.getId());

        assertThat(bookings).hasSize(2);
        assertThat(bookings.get(0).getStart()).isAfterOrEqualTo(bookings.get(1).getStart());
    }

    @Test
    public void findByBooker_IdAndStatusAndStartBeforeAndEndAfterOrderByStart() {
        Booking booking = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByBooker_IdAndStatusAndStartBeforeAndEndAfterOrderByStart(
                booker.getId(), BookingStatus.APPROVED, LocalDateTime.now(), LocalDateTime.now());

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    public void findByBooker_IdAndStartAfterOrderByStart() {
        Booking booking = Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByBooker_IdAndStartAfterOrderByStart(booker.getId(),
                LocalDateTime.now());

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getStart()).isAfter(LocalDateTime.now());
    }

    @Test
    public void findByBooker_IdAndEndBeforeOrderByStartDesc() {
        Booking booking = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(booker.getId(),
                LocalDateTime.now());

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getEnd()).isBefore(LocalDateTime.now());
    }

    @Test
    public void existsByBooker_IdAndItem_IdAndStatusAndEndBefore() {
        Booking booking = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        Boolean exists = bookingRepository.existsByBooker_IdAndItem_IdAndStatusAndEndBefore(
                booker.getId(), item.getId(), BookingStatus.APPROVED, LocalDateTime.now());

        assertThat(exists).isTrue();
    }

    @Test
    public void findByBooker_IdAndStatusOrderByStartDesc() {
        Booking booking = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now())
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(booker.getId(),
                BookingStatus.APPROVED);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    public void findByItem_Owner_IdOrderByStartDesc() {
        Booking booking = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now())
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);


        List<Booking> bookings = bookingRepository.findByItem_Owner_IdOrderByStartDesc(booker.getId());

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getItem().getOwner().getId()).isEqualTo(booker.getId());
    }

    @Test
    public void findByItem_Owner_IdAndStatusAndStartBeforeAndEndAfterOrderByStart() {
        Booking booking = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);


        List<Booking> bookings = bookingRepository.findByItem_Owner_IdAndStatusAndStartBeforeAndEndAfterOrderByStart(
                booker.getId(), BookingStatus.APPROVED, LocalDateTime.now(), LocalDateTime.now());

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    public void findByItem_Owner_IdAndStartAfterOrderByStart() {
        Booking booking = Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByItem_Owner_IdAndStartAfterOrderByStart(booker.getId(),
                LocalDateTime.now());

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getStart()).isAfter(LocalDateTime.now());
    }

    @Test
    public void findByItem_Owner_IdAndEndBeforeOrderByStartDesc() {
        Booking booking = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByItem_Owner_IdAndEndBeforeOrderByStartDesc(booker.getId(),
                LocalDateTime.now());

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getEnd()).isBefore(LocalDateTime.now());
    }

    @Test
    public void findByItem_Owner_IdAndStatusOrderByStartDesc() {
        Booking booking = Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(booker.getId(),
                BookingStatus.APPROVED);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    public void existsByItemIdAndTimeOverlap() {
        Booking booking = Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        boolean exists = bookingRepository.existsByItemIdAndTimeOverlap(item.getId(), LocalDateTime.now().minusDays(2),
                LocalDateTime.now().plusDays(2), List.of(BookingStatus.APPROVED));

        assertThat(exists).isTrue();
    }
}
