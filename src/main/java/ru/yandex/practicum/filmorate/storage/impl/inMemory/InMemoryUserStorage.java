package ru.yandex.practicum.filmorate.storage.impl.inMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {

        users.put(user.getId(), user);
        log.info("{} was added", user);
        return user;
    }

    @Override
    public boolean removeUserById(int userId) {
        User removedUser = users.remove(userId);
        if (removedUser == null) {
            throw new UserNotFoundException("Unknown user: id=" + userId);
        } else {
            log.info("{} was removed", removedUser);
            return true;
        }
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        log.info("{} was updated", user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<User>(users.values());
    }

    @Override
    public User getUserById(int id) {
        return users.get(id);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriendsId().add(friendId);
        log.info("{} is now friend of {}", friend, user);
        friend.getFriendsId().add(userId);
        log.info("{} is now friend of {}", user, friend);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriendsId().remove(friendId);
        log.info("{} is not friend of {} anymore", friend, user);
        friend.getFriendsId().remove(userId);
        log.info("{} is not friend of {} anymore", user, friend);
    }

    @Override
    public List<User> getFriendsOfUserById(int userId) {
        User user = getUserById(userId);
        List<User> friends = user.getFriendsId().stream().map(id -> getUserById(Math.toIntExact(id))).collect(Collectors.toList());
        return friends;
    }

    @Override
    public List<User> getCommonFriendsOf(int user1Id, int user2Id) {
        List<User> friendsOfUser1 = getFriendsOfUserById(user1Id);
        List<User> friendsOfUser2 = getFriendsOfUserById(user2Id);
        List<User> commonFriends = friendsOfUser1.stream().filter(friendOfUser1 -> friendsOfUser2.contains(friendOfUser1)).collect(Collectors.toList());
        return commonFriends;
    }

    @Override
    public Set<Integer> getUsersId() {
        return users.keySet();
    }
}
