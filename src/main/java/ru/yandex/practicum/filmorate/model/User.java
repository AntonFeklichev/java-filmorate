package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import net.bytebuddy.asm.Advice;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class User {
    private int id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    @ToString.Exclude
    @JsonIgnore
    private Set<Integer> friendsId;
    @ToString.Exclude
    @JsonIgnore
    private Set<Integer> likedFilmsId;
}
