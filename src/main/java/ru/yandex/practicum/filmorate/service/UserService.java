package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.validator.IdValidator.validateUserId;

@Slf4j
@Service
public class UserService {
    private static int idForNewUsers = 1;
    private final UserStorage userStorage;


    @Autowired
    public UserService(
            @Qualifier("userDbStorage")
            UserStorage userStorage
    ) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        generateAndSetId(user);
        setNameToLoginIfNameIsEmpty(user);
        setEmptyFriendsIfNull(user);
        setEmptyLikedFilmsIfNull(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        validateUserId(user.getId(), userStorage.getUsersId());
        setNameToLoginIfNameIsEmpty(user);
        setEmptyFriendsIfNull(user);
        setEmptyLikedFilmsIfNull(user);
        return userStorage.updateUser(user);
    }

    public boolean removeUserById(int userId) {
        validateUserId(userId, userStorage.getUsersId());
        return userStorage.removeUserById(userId);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(int id) {
        validateUserId(id, userStorage.getUsersId());
        log.info("user id={} is valid", id);
        return userStorage.getUserById(id);
    }

    public void addFriend(int userId, int friendId) {
        validateUserId(userId, userStorage.getUsersId());
        validateUserId(friendId, userStorage.getUsersId());
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        validateUserId(userId, userStorage.getUsersId());
        validateUserId(friendId, userStorage.getUsersId());
        userStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriendsOfUserById(int userId) {
        validateUserId(userId, userStorage.getUsersId());
        return userStorage.getFriendsOfUserById(userId);
    }

    public List<User> getCommonFriendsOf(int user1Id, int user2Id) {
        validateUserId(user1Id, userStorage.getUsersId());
        validateUserId(user2Id, userStorage.getUsersId());
        return userStorage.getCommonFriendsOf(user1Id, user2Id);
    }

    private void generateAndSetId(User user) {
        user.setId(idForNewUsers);
        idForNewUsers++;
        log.trace("id = {} was generated", user.getId());
    }

    private void setNameToLoginIfNameIsEmpty(User user) {
        String name = user.getName();
        String login = user.getLogin();
        if (name.isBlank()) {
            user.setName(login);
            log.trace("name was blank so login was used as name");
        }
    }

    private void setEmptyFriendsIfNull(User user) {
        if (user.getFriendsId() == null) {
            user.setFriendsId(new HashSet<>(Set.of()));
        }
    }

    private void setEmptyLikedFilmsIfNull(User user) {
        if (user.getLikedFilmsId() == null) {
            user.setLikedFilmsId(new HashSet<>(Set.of()));
        }
    }
}
