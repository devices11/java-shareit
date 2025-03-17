package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponseDto responseDto;

    @BeforeEach
    void setUp() {
        responseDto = UserResponseDto.builder()
                .id(1L)
                .name("John Wick")
                .email("johnWick@test.com")
                .build();
    }

    @Test
    void findById_shouldReturnUser() throws Exception {
        when(userService.findById(1L))
                .thenReturn(responseDto);

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Wick"))
                .andExpect(jsonPath("$.email").value("johnWick@test.com"));
    }

    @Test
    void create_shouldReturnCreatedUser() throws Exception {
        UserCreateRequestDto createRequestDto = UserCreateRequestDto.builder()
                .name("John Wick")
                .email("johnWick@test.com")
                .build();
        when(userService.create(any(UserCreateRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Wick"))
                .andExpect(jsonPath("$.email").value("johnWick@test.com"));
    }

    @Test
    void update_shouldReturnUpdatedUser() throws Exception {
        UserUpdateRequestDto updateRequestDto = UserUpdateRequestDto.builder()
                .name("John Updated")
                .email("johnWick.updated@test.com")
                .build();
        UserResponseDto responseDto = UserResponseDto.builder()
                .id(1L)
                .name("John Updated")
                .email("johnupdated@test.com")
                .build();

        when(userService.update(eq(1L), any(UserUpdateRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.email").value("johnupdated@test.com"));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}