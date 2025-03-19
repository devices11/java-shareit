package ru.practicum.shareit.user;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@DataJpaTest
class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findUserByEmail_shouldReturnUser() {
        User user = User.builder()
                .name("John Wick")
                .email("johnwick@test.com")
                .build();
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findUserByEmail("johnwick@test.com");

        assertTrue(foundUser.isPresent());
        assertEquals("John Wick", foundUser.get().getName());
        assertEquals("johnwick@test.com", foundUser.get().getEmail());
    }

    @Test
    void findUserByEmail_shouldReturnEmpty_whenUserNotFound() {
        Optional<User> foundUser = userRepository.findUserByEmail("notfound@test.com");

        assertFalse(foundUser.isPresent());
    }
}