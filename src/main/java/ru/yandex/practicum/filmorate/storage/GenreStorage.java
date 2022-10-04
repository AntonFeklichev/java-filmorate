package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    Genre getById(int id);

    List<Genre> getAll();

    Set<Integer> getAllId();

    Set<Genre> getByFilmId(int id);

    void addGenresOfFilm(Film film);

    void deleteGenresOfFilm(Film film);
}
