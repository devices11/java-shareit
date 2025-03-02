package ru.practicum.shareit.booking;

public enum State {
    ALL,          // Все бронирования
    CURRENT,      // Текущие бронирования
    PAST,         // Завершённые бронирования
    FUTURE,       // Будущие бронирования
    WAITING,      // Ожидающие подтверждения
    REJECTED;     // Отклонённые

    public static State fromString(String value) {
        return State.valueOf(value.toUpperCase());
    }
}
