package ru.luckyskeet.rolemanagment.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class RoleDtoRequest {

    @NotBlank(message = "Name must not be empty")
    @Size(min = 5, max = 50, message = "Name must be between 6 and 50 characters")
    String name;

    @Size(min = 5, max = 1024, message = "Description must be between 5 and 1024 characters")
    String description;
}
