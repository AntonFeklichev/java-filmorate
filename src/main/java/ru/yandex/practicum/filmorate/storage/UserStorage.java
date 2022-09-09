package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    User addUser(User user);

    User removeUserById(int userId);

    User updateUser(User user);

    List<User> getUsers();

    User getUserById(int id);

    void addFriend(int userId, int friendId);

    User removeFriend(int userId, int friendId);

    List<User> getFriendsOfUserById(int userId);

    List<User> getCommonFriendsOf(int user1Id, int user2Id);

    Set<Integer> getUsersId();
}
