package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.exception.DuplicateException;
import ru.practicum.shareit.util.exception.NotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Пользователь с id = {} не найден", id);
                    return new NotFoundException("Пользователь с id " + id + " не найден");
                });
    }

    @Override
    public User create(User user) {
        validateEmail(user);
        return userRepository.create(user);
    }

    @Override
    public User update(User user) {
        User userFromDB = findById(user.getId());
        validateEmail(user);
        User updatedUser = userFromDB.toBuilder()
                .name(user.getName() != null ? user.getName() : userFromDB.getName())
                .email(user.getEmail() != null ? user.getEmail() : userFromDB.getEmail())
                .build();
        return userRepository.update(updatedUser);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        userRepository.delete(id);
    }

    private void validateEmail(User user) {
        userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(user.getId()))
                .map(User::getEmail)
                .filter(email -> email.equals(user.getEmail()))
                .findFirst()
                .ifPresent(email -> {
                    log.info("Указанный email {} уже существует", email);
                    throw new DuplicateException("Указанный email уже существует");
                });
    }
}
