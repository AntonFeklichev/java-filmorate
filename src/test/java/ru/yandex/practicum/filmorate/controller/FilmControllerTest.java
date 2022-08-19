package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    public void init() {
        filmController = new FilmController();
        film = Film.builder().id(1).name("men in black").description("men in black desc").releaseDate(LocalDate.of(2010, Month.APRIL, 1)).duration(100).build();
    }

    @Test
    public void shouldThrowValidationExceptionWhenUpdatingUnknownFilm() {
        assertThrows(ValidationException.class,
                () -> filmController.updateFilm(film),
                "ValidationException is not thrown when updating an unknown film");
    }

    @Test
    public void shouldNotThrowValidationExceptionWhenUpdatingExistingFilm() {
        filmController.createFilm(film);
        Film updatedFilm = film.toBuilder().name("updated film").build();
        assertDoesNotThrow(() -> filmController.updateFilm(updatedFilm),
                "ValidationException is thrown when updating an existing film");
    }
}
