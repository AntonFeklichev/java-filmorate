package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public User deleteUser(User user) {
        users.remove(user.getId());
        log.info("{} was removed", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        setNameToLoginIfNameIsEmpty(user);
        validateIdForUpdating(user.getId(), users.keySet());
        users.put(user.getId(), user);
        log.info("{} was updated");
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
    public User addFriend(User user, User friend) {
        user.getFriends().add(friend);
        friend.getFriends().add(user);
        log.info("{} and {} are now friends", user, friend);
        return friend;
    }

    @Override
    public User removeFriend(User user, User friend) {
        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        return friend;
    }

    @Override
    public List<User> getFriendsOf(User user) {
        return users.get(user.getId()).getFriends();
    }

    @Override
    public List<User> getCommonFriendsOf(User user1, User user2) {
        List<User> friendsOfUser1 = getFriendsOf(user1);
        List<User> friendsOfUser2 = getFriendsOf(user2);
        List<User> commonFriends = friendsOfUser1.stream().filter(friendOfUser1 -> friendsOfUser2.contains(friendOfUser1)).collect(Collectors.toList());
        return commonFriends;

    }
}
