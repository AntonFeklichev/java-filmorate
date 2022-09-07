package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.UnknownUserException;

import java.util.Set;

public class IdValidatorTest {
    private final Set<Integer> setOfId = Set.of(1, 2, 3, 4, 5);

    @Test
    public void shouldNotThrowExceptionWhenUpdatingExistingId() {
        final int validIdForUpdating = 4;
        Assertions.assertDoesNotThrow(() -> IdValidator.validateUserId(validIdForUpdating, setOfId),
                "Exception is thrown but id is correct");
    }

    @Test
    public void shouldThrowUnknownUserException() {
        final int invalidIdForUpdating = 10;
        Exception ex = Assertions.assertThrows(UnknownUserException.class,
                () -> IdValidator.validateUserId(invalidIdForUpdating, setOfId),
                "UnknownUserException is not thrown but id is not correct");
        Assertions.assertEquals(String.format("Unknown user: %d", invalidIdForUpdating), ex.getMessage());
    }
}
