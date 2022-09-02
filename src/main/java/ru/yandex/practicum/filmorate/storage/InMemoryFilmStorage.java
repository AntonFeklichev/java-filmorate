package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.validator.IdForUpdatingValidator.validateIdForUpdating;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new HashMap<>();
    private static int idForNewFilms = 1;

    @Override
    public Film addFilm(Film film) {
        generateAndSetId(film);
        films.put(film.getId(), film);
        log.info("{} was added", film);
        return film;
    }

    private void generateAndSetId(Film film) {
        film.setId(idForNewFilms++);
        log.debug("id = {} was generated", film.getId());
    }


    @Override
    public Film deleteFilm(Film film) {
        films.remove(film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validateIdForUpdating(film.getId(), films.keySet());
        films.put(film.getId(), film);
        log.info("{} was updated", film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<Film>(films.values());
    }
}
