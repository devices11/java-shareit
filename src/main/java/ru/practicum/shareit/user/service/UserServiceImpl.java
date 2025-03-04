package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.DuplicateException;
import ru.practicum.shareit.util.exception.NotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        User userFromDB = findUserById(id);
        return userMapper.toUserResponseDto(userFromDB);
    }

    @Override
    @Transactional
    public UserResponseDto create(UserCreateRequestDto userDto) {
        checkEmail(userDto.getEmail());
        User user = userMapper.toUser(userDto);
        User createdUser = userRepository.save(user);
        return userMapper.toUserResponseDto(createdUser);
    }

    @Override
    @Transactional
    public UserResponseDto update(Long userId, UserUpdateRequestDto userDto) {
        checkEmail(userDto.getEmail());
        User userFromDB = findUserById(userId);
        User user = userMapper.toUser(userId, userDto);
        User updatedUser = userFromDB.toBuilder()
                .name(user.getName() != null ? user.getName() : userFromDB.getName())
                .email(user.getEmail() != null ? user.getEmail() : userFromDB.getEmail())
                .build();

        User createdUser = userRepository.save(updatedUser);
        return userMapper.toUserResponseDto(createdUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private void checkEmail(String email) {
        if (userRepository.findUserByEmail(email).isPresent()) {
            throw new DuplicateException("Указанный email уже существует");
        }
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }
}
