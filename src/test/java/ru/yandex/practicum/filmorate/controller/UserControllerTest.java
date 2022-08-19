package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private UserController userController;
    private User user;

    @BeforeEach
    public void init() {
        userController = new UserController();
        user = User.builder().id(1).name("vasya").birthday(LocalDate.of(1990, Month.JULY, 11)).email("vasya@yandex.ru").login("vasya_pro").build();
    }


    @Test
    public void shouldThrowValidationExceptionWhenUpdatingUnknownUser() {
        Exception ex = assertThrows(ValidationException.class,
                () -> userController.updateUser(user),
                "ValidationException is not thrown when updating an unknown user");
        assertEquals("Invalid id for updating", ex.getMessage());
    }

    @Test
    public void shouldNotThrowValidationExceptionWhenUpdatingExistingUser() {
        userController.createUser(user);
        User updatedUser = user.toBuilder().name("updated user").build();
        assertDoesNotThrow(() -> userController.updateUser(updatedUser),
                "ValidationException is thrown when updating an existing user");
    }
}
