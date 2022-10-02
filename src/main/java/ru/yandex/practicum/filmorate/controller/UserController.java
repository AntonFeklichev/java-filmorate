package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("creating user: {}", user);
        return userService.addUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/{userId}")
    public boolean removeUserById(@PathVariable int userId) {
        return userService.removeUserById(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable int userId, @PathVariable int friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable int userId, @PathVariable int friendId) {
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> getCommonFriendsOfUserAndFriend(@PathVariable int userId, @PathVariable int friendId){
        return userService.getCommonFriendsOf(userId, friendId);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriendsOfUserById(@PathVariable int userId) {
        return userService.getFriendsOfUserById(userId);
    }
}
