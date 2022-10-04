package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
        return user;
    };

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User addUser(User user) {
        String sql = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(
                sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        return user;
    }

    @Override
    public boolean removeUserById(int userId) {
        String sql = "DELETE FROM USERS WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, userId);
        if (rowsAffected > 0) {
            log.info("user id={} was removed", userId);
            return true;
        } else {
            throw new UserNotFoundException("unknown user: id=" + userId);
        }
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users " +
                "SET email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(
                sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.info("user id={} was updated successfully", user.getId());
        return user;
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM USERS";
        List<User> users = jdbcTemplate.query(sql, userRowMapper);
        return users;
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = jdbcTemplate.query(sql, userRowMapper, id).get(0);
        return user;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sql = "INSERT INTO users_friends (user_id, friend_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
        log.info("user id={} added friend user id={}", userId, friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String sql = "DELETE FROM users_friends " +
                "WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
        log.info("user id={} is not friend of user id={} anymore", userId, friendId);
    }

    @Override
    public List<User> getFriendsOfUserById(int userId) {
        String sql = "SELECT * FROM users " +
                "WHERE id IN (" +
                "SELECT friend_id FROM users_friends " +
                "WHERE user_id = ?)";
        List<User> friends = jdbcTemplate.query(sql, userRowMapper, userId);
        return friends;
    }

    @Override
    public List<User> getCommonFriendsOf(int user1Id, int user2Id) {
        String sql = "SELECT *\n" +
                "FROM users\n" +
                "WHERE id IN (\n" +
                "    SELECT friend_id\n" +
                "    FROM users_friends\n" +
                "    WHERE user_id = ?\n" +
                "    )\n" +
                "  AND ID IN (\n" +
                "    SELECT friend_id\n" +
                "    FROM users_friends\n" +
                "    WHERE user_id = ?\n" +
                "    );";
        List<User> commonFriends = jdbcTemplate.query(sql, userRowMapper, user1Id, user2Id);
        return commonFriends;
    }

    @Override
    public Set<Integer> getUsersId() {
        String sql = "SELECT id FROM users";
        Set<Integer> usersId = new HashSet<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()) {
            usersId.add(rowSet.getInt("id"));
        }
        return usersId;
    }
}
