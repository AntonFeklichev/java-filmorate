package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.annotation.ReleaseDateValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Month;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateValidation, LocalDate> {
    private static final LocalDate FIRST_MOVIE_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return !localDate.isBefore(FIRST_MOVIE_RELEASE_DATE);
    }
}
