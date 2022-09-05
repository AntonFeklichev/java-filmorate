package ru.yandex.practicum.filmorate.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.UnknownUserException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
public class UserControllerExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUnknownUser(final UnknownUserException e) {
        return new ErrorResponse(e.getMessage());
    }
}
