package ru.yandex.practicum.filmorate.exception;

public class GenreNotFoundException extends DataNotFoundException {
    public GenreNotFoundException(String msg) {
        super(msg);
    }
}
