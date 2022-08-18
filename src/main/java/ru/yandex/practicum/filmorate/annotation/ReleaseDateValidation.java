package ru.yandex.practicum.filmorate.annotation;

import ru.yandex.practicum.filmorate.validator.ReleaseDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ReleaseDateValidator.class)
public @interface ReleaseDateValidation {
    public String message() default "Invalid release date: must be after December 28, 1895";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
