package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validator.IdForUpdatingValidator.validateIdForUpdating;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private static int idForNewUsers = 1;
    private Map<Integer, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        generateAndSetId(user);
        setNameToLoginIfNameIsEmpty(user);
        setEmptyFriendsIfNull(user);
        users.put(user.getId(), user);
        log.info("{} was added", user);
        return user;
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
        if (user.getFriends() == null) {
            user.setFriends(new ArrayList<>(List.of()));
        }
    }

    @Override
    public User removeUserById(int userId) {
        User removedUser = users.remove(userId);
        log.info("{} was removed", removedUser);
        return removedUser;
    }

    @Override
    public User updateUser(User user) {
        setNameToLoginIfNameIsEmpty(user);
        setEmptyFriendsIfNull(user);
        validateIdForUpdating(user.getId(), users.keySet());
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
    public User addFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().add(friend);
        log.info("{} is now friend of {}", friend, user);
        friend.getFriends().add(user);
        log.info("{} is now friend of {}", user, friend);
        log.info("{} and {} are now friends of each other", user, friend);
        return null;
    }

    @Override
    public User removeFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        return friend;
    }

    @Override
    public List<User> getFriendsOfUserById(int userId) {
        return getUserById(userId).getFriends();
    }

    @Override
    public List<User> getCommonFriendsOf(int user1Id, int user2Id) {
        List<User> friendsOfUser1 = getFriendsOfUserById(user1Id);
        List<User> friendsOfUser2 = getFriendsOfUserById(user2Id);
        List<User> commonFriends = friendsOfUser1.stream().filter(friendOfUser1 -> friendsOfUser2.contains(friendOfUser1)).collect(Collectors.toList());
        return commonFriends;
    }
}
