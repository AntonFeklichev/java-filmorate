package ru.yandex.practicum.filmorate.validator;

import javax.validation.ValidationException;
import java.util.Collection;

public class IdForUpdatingValidator {
    public static void validateIdForUpdating(int id, Collection<Integer> existingId) {
        if (!existingId.contains(id)) throw new ValidationException("Invalid id for updating");
    }
}
