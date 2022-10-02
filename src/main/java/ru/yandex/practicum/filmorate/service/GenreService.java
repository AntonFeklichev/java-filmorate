package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.validator.IdValidator.validateGenreId;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getById(int id){
        validateGenreId(id, genreStorage.getAllId());
        return genreStorage.getById(id);
    }

    public List<Genre> getAll(){
        return genreStorage.getAll();
    }

}
