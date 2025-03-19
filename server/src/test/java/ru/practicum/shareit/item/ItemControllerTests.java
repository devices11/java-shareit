package ru.practicum.shareit.item;

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
import ru.practicum.shareit.booking.dto.BookingForItemResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
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
@WebMvcTest(ItemController.class)
public class ItemControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private ItemResponseDto itemResponseDto;
    private ItemOwnerResponseDto itemOwnerResponseDto;
    private CommentResponseDto comments;

    @BeforeEach
    void setUp() {
        UserResponseDto booker = UserResponseDto.builder()
                .id(1L)
                .name("John Snow")
                .email("johnsnow@test.com")
                .build();
        BookingForItemResponseDto lastBooking = BookingForItemResponseDto.builder()
                .start(LocalDateTime.of(2025, 3, 11, 12, 0, 0))
                .end(LocalDateTime.of(2025, 3, 12, 12, 0, 0))
                .status(BookingStatus.APPROVED)
                .booker(booker)
                .build();
        BookingForItemResponseDto nextBooking = BookingForItemResponseDto.builder()
                .start(LocalDateTime.of(2025, 4, 11, 12, 0, 0))
                .end(LocalDateTime.of(2025, 4, 12, 12, 0, 0))
                .status(BookingStatus.APPROVED)
                .booker(booker)
                .build();
        comments = CommentResponseDto.builder()
                .id(1L)
                .text("This is a comment")
                .authorName("John Wick")
                .created(LocalDateTime.of(2025, 3, 11, 13, 0, 0))
                .build();

        itemResponseDto = ItemResponseDto.builder()
                .id(1L)
                .name("Test Item")
                .description("Test description")
                .available(true)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(List.of(comments))
                .requestId(1L)
                .build();

        itemOwnerResponseDto = ItemOwnerResponseDto.builder()
                .id(2L)
                .name("Test Item 2")
                .description("Test description 2")
                .available(true)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();
    }

    @Test
    void findById_shouldReturnItemResponseDto() throws Exception {
        when(itemService.findById(anyLong()))
                .thenReturn(itemResponseDto);

        mockMvc.perform(get("/items/{id}", itemResponseDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemResponseDto)));
    }

    @Test
    void findAllForUser_shouldReturnItemOwnerResponseDto() throws Exception {
        when(itemService.findAllForOwner(anyLong()))
                .thenReturn(List.of(itemOwnerResponseDto));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 11L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemOwnerResponseDto))));
    }

    @Test
    void findByText_shouldReturnItemResponseDto() throws Exception {
        when(itemService.findByText(anyString()))
                .thenReturn(List.of(itemResponseDto));

        mockMvc.perform(get("/items/search")
                        .param("text", "description 2"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemResponseDto))));
    }

    @Test
    void create_shouldReturnItemResponseDto() throws Exception {
        when(itemService.create(anyLong(), any(ItemCreateRequestDto.class)))
                .thenReturn(itemResponseDto);
        ItemCreateRequestDto requestDto = ItemCreateRequestDto.builder()
                .name("Test name 3")
                .description("Test description 3")
                .available(true)
                .requestId(1L)
                .build();

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(itemResponseDto)));
    }

    @Test
    void update_shouldReturnItemResponseDto() throws Exception {
        when(itemService.update(anyLong(), anyLong(), any(ItemUpdateRequestDto.class)))
                .thenReturn(itemResponseDto);
        ItemUpdateRequestDto itemUpdateRequestDto = ItemUpdateRequestDto.builder()
                .name("Test name 3")
                .description("Test description 3")
                .available(true)
                .build();

        mockMvc.perform(patch("/items/{id}", 12)
                        .header("X-Sharer-User-Id", 12)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemUpdateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemResponseDto)));
    }

    @Test
    void createComment_shouldReturnCommentResponseDto() throws Exception {
        when(itemService.createComment(anyLong(), anyLong(), any(CommentRequestDto.class)))
                .thenReturn(comments);
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .text("Text test")
                .build();

        mockMvc.perform(post("/items/{itemId}/comment", 1)
                        .header("X-Sharer-User-Id", 12)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(comments)));
    }
}
