package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.util.exception.DuplicateException;
import ru.practicum.shareit.util.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class UserServiceImplTests {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("John Wick")
                .email("johnwick@test.com")
                .build();
        userRepository.save(user);
    }

    @Test
    void findById_shouldReturnUserResponseDto() {

        UserResponseDto result = userService.findById(user.getId());

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void findById_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> userService.findById(1111L));
    }

    @Test
    void create_shouldReturnCreatedUser() {
        UserCreateRequestDto createRequestDto = UserCreateRequestDto.builder()
                .name("John Wick 2")
                .email("johnwick2@test.com")
                .build();

        UserResponseDto result = userService.create(createRequestDto);

        assertNotNull(result);
        assertEquals(createRequestDto.getName(), result.getName());
        assertEquals(createRequestDto.getEmail(), result.getEmail());
    }

    @Test
    void create_shouldThrowDuplicateException() {
        UserCreateRequestDto createRequestDto = UserCreateRequestDto.builder()
                .name("John Wick")
                .email("johnwick@test.com")
                .build();
        assertThrows(DuplicateException.class, () -> userService.create(createRequestDto));
    }

    @Test
    void update_shouldReturnUpdatedUser() {
        UserUpdateRequestDto updatedUser = UserUpdateRequestDto.builder()
                .name("John Updated")
                .email("johnwick.updated@test.com")
                .build();

        UserResponseDto result = userService.update(user.getId(), updatedUser);

        assertNotNull(result);
        assertEquals(updatedUser.getName(), result.getName());
        assertEquals(updatedUser.getEmail(), result.getEmail());
    }

    @Test
    void update_shouldThrowNotFoundException() {
        UserUpdateRequestDto updatedUser = UserUpdateRequestDto.builder()
                .name("John Wick")
                .email("johnwick22@test.com")
                .build();
        assertThrows(NotFoundException.class, () -> userService.update(111L, updatedUser));
    }

    @Test
    void update_shouldThrowDuplicateException() {
        User user2 = User.builder()
                .name("John Wick22")
                .email("johnwick22@test.com")
                .build();
        userRepository.save(user2);
        UserUpdateRequestDto updatedUser = UserUpdateRequestDto.builder()
                .name("John Wick")
                .email("johnwick@test.com")
                .build();
        assertThrows(DuplicateException.class, () -> userService.update(user2.getId(), updatedUser));
    }

    @Test
    void delete_shouldDeleteUser() {
        User user2 = User.builder()
                .name("John Wick22")
                .email("johnwick22@test.com")
                .build();
        userRepository.save(user2);
        assertNotNull(userService.findById(user2.getId()));

        userService.delete(user2.getId());

        assertThrows(NotFoundException.class, () -> userService.findById(user2.getId()));
    }
}