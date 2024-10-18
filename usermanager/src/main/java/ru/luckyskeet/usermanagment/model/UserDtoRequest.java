package ru.luckyskeet.usermanagment.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class UserDtoRequest {

    @NotBlank(message = "must not be empty")
    @Size(min = 5, max = 50, message = "must be between 5 and 50 characters")
    String name;

    @Email(message = " should be valid")
    @NotBlank(message = "must not be empty")
    String email;

    @NotBlank(message = "must not be empty")
    @Size(min = 5, max = 50, message = "must be between 5 and 50 characters")
    String password;
}