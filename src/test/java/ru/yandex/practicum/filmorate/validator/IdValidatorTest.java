package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Set;

public class IdValidatorTest {
    private final Set<Integer> setOfId = Set.of(1, 2, 3, 4, 5);

    @Test
    public void shouldNotThrowValidationExceptionWhenUpdatingExistingId() {
        final int validIdForUpdating = 4;
        Assertions.assertDoesNotThrow(() -> IdValidator.validateUserId(validIdForUpdating, setOfId),
                "ValidationException is thrown but id is correct");
    }

    @Test
    public void shouldThrowValidationExceptionWhenUpdatingUnknownId() {
        final int invalidIdForUpdating = 10;
        Exception ex = Assertions.assertThrows(ValidationException.class,
                () -> IdValidator.validateUserId(invalidIdForUpdating, setOfId),
                "ValidationException is not thrown but id is not correct");
        Assertions.assertEquals("Invalid id for updating", ex.getMessage());
    }
}
