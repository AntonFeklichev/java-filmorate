package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.UnknownUserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Collection;

public class IdValidator {
    public static void validateUserId(int id, Collection<Integer> existingId) throws UnknownUserException {
        String msg = String.format("Unknown user: %d", id);
        if (!existingId.contains(id)) throw new UnknownUserException(msg);
    }
//
//    public static void validateFilmId(int id, Collection<Integer> existingId) throws
}
