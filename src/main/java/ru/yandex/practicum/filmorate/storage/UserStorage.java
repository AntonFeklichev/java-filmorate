package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    User deleteUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    User getUserById(int id);

    User addFriend(User user, User Friend);

    User removeFriend(User user, User friend);

    List<User> getFriendsOf(User user);

    List<User> getCommonFriendsOf(User user1, User user2);
}
