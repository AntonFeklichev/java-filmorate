package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.validator.IdValidator.validateFilmId;
import static ru.yandex.practicum.filmorate.validator.IdValidator.validateUserId;

@Slf4j
@Service
public class FilmService {

    private static int idForNewFilms = 1;

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage,
                       @Qualifier("userDbStorage")
                       UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        generateAndSetId(film);
        setEmptyLikedUsersIfNull(film);
        return filmStorage.addFilm(film);
    }

    public Film deleteFilm(Film film) {
        return filmStorage.deleteFilm(film);
    }

    public Film updateFilm(Film film) {
        validateFilmId(film.getId(), filmStorage.getFilmsId());
        setEmptyLikedUsersIfNull(film);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(int filmId) {
        validateFilmId(filmId, filmStorage.getFilmsId());
        return filmStorage.getFilmById(filmId);
    }

    public void likeFilm(int filmId, int userId) {
        validateFilmId(filmId, filmStorage.getFilmsId());
        validateUserId(userId, userStorage.getUsersId());
        filmStorage.likeFilm(filmId, userId);
    }

    public void deleteLikeOfFilm(int filmId, int userId) {
        validateFilmId(filmId, filmStorage.getFilmsId());
        validateUserId(userId, userStorage.getUsersId());
        filmStorage.deleteLikeOfFilm(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    private void setEmptyLikedUsersIfNull(Film film) {
        if (film.getLikedUsersId() == null) {
            film.setLikedUsersId(new HashSet<>(Set.of()));
        }
    }

    private void generateAndSetId(Film film) {
        film.setId(idForNewFilms++);
        log.debug("id = {} was generated", film.getId());
    }
}
