package ru.luckyskeet.rolemanagment.exception;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@FieldDefaults(level = AccessLevel.PUBLIC)
public class ExceptionControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ExceptionResponse handleGetUser(final RoleNotFoundException exception) {
        return new ExceptionResponse().setMessage(exception.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionResponse handleValidationExceptions(final MethodArgumentNotValidException exception) {
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        exception.getBindingResult().getFieldErrors()
                .forEach(fieldError -> errorMessage
                        .append(fieldError.getField())
                        .append(": ")
                        .append(fieldError.getDefaultMessage())
                        .append(". "));
        return new ExceptionResponse().setMessage(errorMessage.toString());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.CONFLICT)
    ExceptionResponse handleDBExceptions(final DataIntegrityViolationException exception) {
        String message = exception.getMessage();
        String userFriendlyMessage;
        if (message.contains("duplicate key")) {
            userFriendlyMessage = "Role already exists with the provided details.";
        } else {
            userFriendlyMessage = "An unexpected error occurred. Please try again.";
        }
        return new ExceptionResponse().setMessage(userFriendlyMessage);
    }
}
