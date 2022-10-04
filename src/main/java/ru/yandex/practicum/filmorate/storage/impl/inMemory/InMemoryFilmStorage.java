package ru.yandex.practicum.filmorate.storage.impl.inMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final UserStorage userStorage;
    Comparator<Film> likesDescendingComparator = (film1, film2) -> -(film1.getLikedUsersId().size() - film2.getLikedUsersId().size());
    private Map<Integer, Film> films = new HashMap<>();

    @Autowired
    public InMemoryFilmStorage(
            @Qualifier("userDbStorage")
            UserStorage userStorage
    ) {
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        films.put(film.getId(), film);
        log.info("{} was added", film);
        return film;
    }

    public boolean removeFilmById(int filmId) {
        boolean removed = films.remove(filmId) != null;
        if (removed) {
            log.info("film id={} was removed", filmId);
        }
        return removed;
    }

    public Film updateFilm(Film film) throws FilmNotFoundException {
        films.put(film.getId(), film);
        log.info("{} was updated", film);
        return film;
    }

    public List<Film> getFilms() {
        return new ArrayList<Film>(films.values());
    }

    public Film getFilmById(int filmId) throws FilmNotFoundException {
        return films.get(filmId);
    }

    public void likeFilm(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        film.getLikedUsersId().add(userId);
        user.getLikedFilmsId().add(filmId);
    }

    public void deleteLikeOfFilm(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        film.getLikedUsersId().remove(userId);
        user.getLikedFilmsId().remove(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        return films.values().stream().sorted(likesDescendingComparator).limit(count).collect(Collectors.toList());
    }

    public Set<Integer> getFilmsId() {
        return films.keySet();
    }
}
