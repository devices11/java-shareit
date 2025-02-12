package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

public interface UserService {
    User findById(Long id);

    User create(User user);

    User update(User user);

    void delete(Long id);
}