package ru.luckyskeet.coordinator.exception;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@FieldDefaults(level = AccessLevel.PUBLIC)
public class ExceptionControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ExceptionResponse handleDBExceptions(final DataIntegrityViolationException exception) {
        String message = exception.getMessage();
        String userFriendlyMessage;
        if (message.contains("user_roles_user_id_fkey")) {
            userFriendlyMessage = "No users with such ID";
        } else if (message.contains("user_roles_role_id_fkey")){
            userFriendlyMessage = "No roles with such ID";
        } else {
            userFriendlyMessage = "An unexpected error occurred. Please try again.";
        }
        return new ExceptionResponse().setMessage(userFriendlyMessage);
    }

}
