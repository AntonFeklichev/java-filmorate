package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final LocalDate FIRST_MOVIE_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private Map<Integer, Film> films = new HashMap<>();
    private int idForNewFilms = 1;

    private void addNewFilm(Film film) {
        generateAndSetId(film);
        films.put(film.getId(), film);
    }

    private void generateAndSetId(Film film) {
        film.setId(idForNewFilms);
        idForNewFilms++;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        addNewFilm(film);
        log.info("{} was added", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("{} was updated", film);
        } else {
            throw new ValidationException("Invalid film id for updating");
        }
        return film;
    }

}
