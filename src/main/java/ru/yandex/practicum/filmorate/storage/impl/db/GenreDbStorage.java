package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
@Qualifier("genreDbStorage")
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Genre> genreRowMapper = (rs, rowNum) -> {
        Genre genre = Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
        return genre;
    };

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getById(int id) {
        String sql = "SELECT * FROM genre " +
                "WHERE id = ?";
        Genre genre = jdbcTemplate.queryForObject(sql, genreRowMapper, id);
        return genre;
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM genre " +
                "ORDER BY ID ASC";
        List<Genre> allGenres = jdbcTemplate.query(sql, genreRowMapper);
        return allGenres;
    }

    @Override
    public Set<Integer> getAllId() {
        String sql = "SELECT id FROM genre";
        Set<Integer> allId = new HashSet<>(
                jdbcTemplate.query(sql, (rs, rowNum) ->
                        rs.getInt("id")
                )
        );
        return allId;
    }

    @Override
    public Set<Genre> getByFilmId(int id) {
        String sql = "SELECT * FROM films_genres fg " +
                "LEFT JOIN genre g on g.id = fg.genre_id " +
                "WHERE fg.film_id = ?";
        Set<Genre> genresOfFilm = new HashSet<>(
                jdbcTemplate.query(sql, genreRowMapper, id)
        );
        log.info("GENRES: {}", genresOfFilm);
        return genresOfFilm;
    }

    public void addGenresOfFilm(Film film) {
        String sql = "INSERT INTO films_genres (film_id, genre_id) " +
                "VALUES (?, ?)";
        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            for (Genre genre : genres) {
                jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, genre.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genres.size();
                    }
                });
            }
        }
    }
}
