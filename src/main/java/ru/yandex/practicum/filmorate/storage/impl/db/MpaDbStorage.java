package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
@Qualifier("mpaDbStorage")
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Mpa> mpaRowMapper = (rs, rowNum) -> {
        Mpa mpa = Mpa.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
        return mpa;
    };

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT * FROM mpa";
        List<Mpa> allMpa = jdbcTemplate.query(sql, mpaRowMapper);
        return allMpa;
    }

    @Override
    public Mpa getById(int mpaId) {
        String sql = "SELECT * FROM mpa " +
                "WHERE id = ?";
        Mpa mpa = jdbcTemplate.queryForObject(sql, mpaRowMapper, mpaId);
        return mpa;
    }

    @Override
    public Set<Integer> getAllId() {
        String sql = "SELECT id FROM mpa";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        Set<Integer> allId = new HashSet<>();
        while (rowSet.next()){
            allId.add(rowSet.getInt("id"));
        }
        return allId;
    }
}
