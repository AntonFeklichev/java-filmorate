package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    Film addFilm(Film film);

    boolean removeFilmById(int filmId);

    Film updateFilm(Film film);

    List<Film> getFilms();

    Film getFilmById(int filmId);

    void likeFilm(int filmId, int userId);

    void deleteLikeOfFilm(int filmId, int userId);

    List<Film> getPopularFilms(int count);

    Set<Integer> getFilmsId();
}
