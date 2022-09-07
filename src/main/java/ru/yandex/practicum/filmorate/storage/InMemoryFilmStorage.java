package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UnknownFilmException;
import ru.yandex.practicum.filmorate.exception.UnknownUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

import static ru.yandex.practicum.filmorate.validator.IdValidator.validateFilmId;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static int idForNewFilms = 1;
    private final UserStorage userStorage;
    private Map<Integer, Film> films = new HashMap<>();

    @Autowired
    public InMemoryFilmStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

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
        log.info("{} was removed", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws UnknownFilmException {
        validateFilmId(film.getId(), films.keySet());
        films.put(film.getId(), film);
        log.info("{} was updated", film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<Film>(films.values());
    }

    @Override
    public Film getFilmById(int filmId) throws UnknownFilmException {
        validateFilmId(filmId, films.keySet());
        return films.get(filmId);
    }

    @Override
    public void likeFilm(int filmId, int userId) throws UnknownFilmException, UnknownUserException {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        setEmptyLikedUsersIfNull(film);
        setEmptyLikedFilmsIfNull(user);
        film.getLikedUsersId().add(userId);
        user.getLikedFilmsId().add(filmId);
    }

    private void setEmptyLikedFilmsIfNull(User user) {
        if (user.getLikedFilmsId() == null) {
            user.setLikedFilmsId(new HashSet<>(Set.of()));
        }
    }

    private void setEmptyLikedUsersIfNull(Film film) {
        if (film.getLikedUsersId() == null) {
            film.setLikedUsersId(new HashSet<>(Set.of()));
        }
    }

}
