package ru.luckyskeet.usermanagment.exception;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {
    public UserNotFoundException(Long userId) {
        super("No user with such ID: " + userId);
    }
}
