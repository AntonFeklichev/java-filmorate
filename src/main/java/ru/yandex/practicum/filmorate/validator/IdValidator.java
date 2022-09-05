package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Collection;

public class IdValidator {
    public static void validateUserId(int id, Collection<Integer> existingId) throws ValidationException {
        if (!existingId.contains(id)) throw new ValidationException("Unknown user");
    }
}
