package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Set;

public class IdForUpdatingValidatorTest {
    final Set<Integer> setOfId = Set.of(1, 2, 3, 4, 5);
    final int validIdForUpdating = 4;
    final int invalidIdForUpdating = 10;

    @Test
    public void idForUpdating() {
        Exception ex = Assertions.assertThrows(ValidationException.class,
                () -> IdForUpdatingValidator.validateIdForUpdating(invalidIdForUpdating, setOfId));
        Assertions.assertEquals("Invalid id for updating", ex.getMessage());
        Assertions.assertDoesNotThrow(() -> IdForUpdatingValidator.validateIdForUpdating(validIdForUpdating, setOfId));
    }
}
