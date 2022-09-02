package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User deleteUser(User user) {
        return userStorage.deleteUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User addFriend(User user, User friend) {
        return userStorage.addFriend(user, friend);
    }

    public User removeFriend(User user, User friend) {
        return userStorage.removeFriend(user, friend);
    }

    public List<User> getFriendsOf(User user) {
        return userStorage.getFriendsOf(user);
    }

    public List<User> getCommonFriendsOf(User user1, User user2) {
        return userStorage.getCommonFriendsOf(user1, user2);
    }
}
