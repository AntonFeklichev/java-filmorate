package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.validator.IdForUpdatingValidator.validateIdForUpdating;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Integer, User> users = new HashMap<>();

    private int idForNewUsers = 1;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        addNewUser(user);
        return user;
    }

    private void addNewUser(User user) {
        generateAndSetId(user);
        setNameToLoginIfNameIsEmpty(user);
        users.put(user.getId(), user);
    }

    private void generateAndSetId(User user) {
        user.setId(idForNewUsers);
        idForNewUsers++;
    }

    private void setNameToLoginIfNameIsEmpty(User user) {
        String name = user.getName();
        String login = user.getLogin();
        if (name.isBlank()) user.setName(login);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        setNameToLoginIfNameIsEmpty(user);
        validateIdForUpdating(user.getId(), users.keySet());
        users.put(user.getId(), user);
        return user;
    }

}
