package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(BookingController.class)
public class BookingControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private BookingResponseDto bookingResponseDto;

    @BeforeEach
    void setUp() {
        UserResponseDto booker = UserResponseDto.builder()
                .id(1L)
                .name("Booker")
                .email("booker@example.com")
                .build();
        ItemResponseDto item = ItemResponseDto.builder()
                .id(1L)
                .name("Item")
                .description("Item")
                .available(true)
                .lastBooking(null)
                .nextBooking(null)
                .comments(null)
                .build();
        bookingResponseDto = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2025, 3, 11, 12, 0, 0))
                .end(LocalDateTime.of(2025, 3, 12, 12, 0, 0))
                .status(BookingStatus.WAITING)
                .booker(booker)
                .item(item)
                .build();
    }

    @Test
    void findById_shouldReturnBookingResponseDto() throws Exception {
        when(bookingService.findById(anyLong(), anyLong()))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingResponseDto)));
    }

    @Test
    void findAllByUserId_shouldReturnBookingResponseDto() throws Exception {
        when(bookingService.findAllByUserId(anyLong(), any(State.class)))
                .thenReturn(List.of(bookingResponseDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 11L)
                        .param("state", State.WAITING.name()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookingResponseDto))));
    }

    @Test
    void findAllByOwnerId_shouldReturnBookingResponseDto() throws Exception {
        when(bookingService.findAllByOwnerId(anyLong(), any(State.class)))
                .thenReturn(List.of(bookingResponseDto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 11L)
                        .param("state", State.WAITING.name()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookingResponseDto))));
    }

    @Test
    void create_shouldReturnBookingResponseDto() throws Exception {
        when(bookingService.create(anyLong(), any(BookingRequestDto.class)))
                .thenReturn(bookingResponseDto);
        BookingRequestDto requestDto = BookingRequestDto.builder()
                .itemId(2L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingResponseDto)));
    }

    @Test
    void updateStatus_shouldReturnBookingResponseDto() throws Exception {
        when(bookingService.updateStatus(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 3)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingResponseDto)));
    }
}
