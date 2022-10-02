package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

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
}
