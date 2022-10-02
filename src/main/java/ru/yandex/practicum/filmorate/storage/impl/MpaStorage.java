package ru.yandex.practicum.filmorate.storage.impl;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Set;

public interface MpaStorage {
    List<Mpa> getAll();

    Mpa getById(int mpaId);
    Set<Integer> getAllId();
}
