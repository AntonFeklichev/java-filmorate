package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.MpaStorage;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final RowMapper<Film> filmRowMapper = (rs, rowNum) -> {
        Film film = Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getInt("duration"))
                .releaseDate(rs.getDate("release_date"))
                .mpa(getMpaById(rs.getInt("mpa_id")))
                .build();
        return film;
    };

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
    }

    private Mpa getMpaById(int mpaId) {
        return mpaStorage.getById(mpaId);
    }

    @Override
    public Film addFilm(Film film) {
        String sql = "INSERT INTO films (name, description, duration, release_date, mpa_id)" +
                "VALUES (?, ?, ?, ?, ?)";
        log.info("adding film: {}", film);
        int rowsAffected = jdbcTemplate.update(
                sql,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId()
        );
        if (rowsAffected > 0) {
            log.info("film id={} added successfully", film.getId());
            return film;
        } else {
            log.info("error occurred");
            throw new ValidationException();
        }
    }

    @Override
    public boolean removeFilmById(int filmId) {
        String sql = "DELETE FROM films WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, filmId);
        if (rowsAffected > 0) {
            log.info("film id={} was removed successfully", filmId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films " +
                "SET name = ?, description = ?, duration = ?, release_date = ?, mpa_id = ? " +
                "WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(
                sql,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM films";
        List<Film> films = jdbcTemplate.query(sql, filmRowMapper);
        return films;
    }

    @Override
    public Film getFilmById(int filmId) {
        String sql = "SELECT * FROM films " +
                "WHERE id = ?";
        log.info("GETTING BY ID");

        Film film = jdbcTemplate.queryForObject(sql, filmRowMapper, filmId);
        log.info("GETTING BY ID");
        return film;
    }

    @Override
    public void likeFilm(int filmId, int userId) {

    }

    @Override
    public void deleteLikeOfFilm(int filmId, int userId) {

    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return null;
    }

    @Override
    public Set<Integer> getFilmsId() {
        String sql = "SELECT id FROM  films";
        Set<Integer> filmsId = new HashSet<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()) {
            filmsId.add(rowSet.getInt("id"));
        }
        return filmsId;
    }
}