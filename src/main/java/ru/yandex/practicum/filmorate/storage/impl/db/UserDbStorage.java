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

import javax.validation.ValidationException;
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
        int affectedRows = jdbcTemplate.update(
                sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        if (affectedRows > 0) {
            return user;
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public boolean removeUserById(int userId) {
        String sql = "DELETE FROM USERS WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, userId);
        if (rowsAffected > 0) {
            log.info("User id={} was removed", userId);
            return true;
        } else {
            throw new UserNotFoundException("Unknown user: id=" + userId);
        }
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users " +
                "SET email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(
                sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        if (rowsAffected > 0) {
            log.info("user id=? was updated successfully");
            return user;
        } else {
            throw new ValidationException();
        }
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
        log.info("found user id={}", id);
        return user;
    }

    @Override
    public void addFriend(int userId, int followerId) {
        String sql = "INSERT INTO USERS_FOLLOWERS (USER_ID, FOLLOWER_ID) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, followerId);
    }

    @Override
    public void removeFriend(int userId, int followerId) {
        String sql = "DELETE FROM USERS_FOLLOWERS " +
                "WHERE user_id = ? AND follower_id = ?";
        jdbcTemplate.update(sql, userId, followerId);
    }

    @Override
    public List<User> getFriendsOfUserById(int userId) {
        String sql = "SELECT * FROM users " +
                "WHERE id IN (" +
                "SELECT follower_id FROM users_followers " +
                "WHERE user_id = ?)";
        List<User> friends = jdbcTemplate.query(sql, userRowMapper, userId);
        return friends;
    }

    @Override
    public List<User> getCommonFriendsOf(int user1Id, int user2Id) {
        String sql = "SELECT *\n" +
                "FROM USERS\n" +
                "WHERE ID IN (\n" +
                "    SELECT FOLLOWER_ID\n" +
                "    FROM USERS_FOLLOWERS\n" +
                "    WHERE USER_ID = ?\n" +
                "    )\n" +
                "  AND ID IN (\n" +
                "    SELECT FOLLOWER_ID\n" +
                "    FROM USERS_FOLLOWERS\n" +
                "    WHERE USER_ID = ?\n" +
                "    );";
        List<User> commonFriends = jdbcTemplate.query(sql, userRowMapper, user1Id, user2Id);
        return commonFriends;
    }

    @Override
    public Set<Integer> getUsersId() {
        String sql = "SELECT id FROM USERS";
        Set<Integer> usersId = new HashSet<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()) {
            usersId.add(rowSet.getInt("id"));
        }
        return usersId;
    }
}
