package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.MpaStorage;

import java.util.List;

@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(@Qualifier("mpaDbStorage")
                      MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<Mpa> getAll() {
        return mpaStorage.getAll();
    }

    public Mpa getById(int mpaId) {
        //TODO:validate mpaId
        return mpaStorage.getById(mpaId);
    }
}
