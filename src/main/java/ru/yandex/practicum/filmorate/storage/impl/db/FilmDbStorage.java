package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final RowMapper<Film> filmRowMapper = (rs, rowNum) -> {
        Film film = Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getInt("duration"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .mpa(getMpaById(rs.getInt("mpa_id")))
                .genres(getGenresByFilmId(rs.getInt("id")))
                .build();
        return film;
    };

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    private Set<Genre> getGenresByFilmId(int id) {
        return genreStorage.getByFilmId(id);
    }

    private Mpa getMpaById(int mpaId) {
        return mpaStorage.getById(mpaId);
    }

    @Override
    public Film addFilm(Film film) {
        String sql = "INSERT INTO films (name, description, duration, release_date, mpa_id)" +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setInt(3, film.getDuration());
            ps.setDate(4, Date.valueOf(film.getReleaseDate()));
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        genreStorage.addGenresOfFilm(film);
        log.info("film id={} added successfully", film.getId());
        return film;
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
        jdbcTemplate.update(
                sql,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId()
        );
        genreStorage.deleteGenresOfFilm(film);
        genreStorage.addGenresOfFilm(film);
        film.setGenres(
                genreStorage.getByFilmId(film.getId())
        );
        log.info("film id={} was updated successfully", film.getId());
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
        Film film = jdbcTemplate.queryForObject(sql, filmRowMapper, filmId);
        return film;
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        String sql = "INSERT INTO films_likes (film_id, liked_user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLikeOfFilm(int filmId, int userId) {
        String sql = "DELETE FROM films_likes " +
                "WHERE film_id = ? AND liked_user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sql = "SELECT * FROM films " +
                "LEFT JOIN (" +
                "SELECT " +
                "film_id," +
                "COUNT(distinct liked_user_id) AS rate " +
                "FROM films_likes " +
                "GROUP BY film_id) " +
                "AS popular ON films.id = popular.film_id " +
                "ORDER BY popular.rate DESC " +
                "LIMIT ?";
        List<Film> popularFilms = jdbcTemplate.query(sql, filmRowMapper, count);
        return popularFilms;
    }

    @Override
    public Set<Integer> getFilmsId() {
        String sql = "SELECT id FROM films";
        Set<Integer> filmsId = new HashSet<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()) {
            filmsId.add(rowSet.getInt("id"));
        }
        return filmsId;
    }
}