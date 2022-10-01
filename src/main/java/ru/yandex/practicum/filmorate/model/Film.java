package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.ReleaseDateValidation;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {
    private int id;
    @NotBlank(message = "Film name should not be blank")
    private String name;
    @Size(max = 200, message = "Film description length should be less than 200 characters long")
    private String description;
    @ReleaseDateValidation
    private Date releaseDate;
    @Positive(message = "Film duration should be a positive number")
    private int duration;
    private Set<Integer> likedUsersId;
    private Genre genre;
    private Mpa mpa;
}
