package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
