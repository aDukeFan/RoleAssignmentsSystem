package ru.luckyskeet.rolemanagment.exception;

import java.util.NoSuchElementException;

public class RoleNotFoundException extends NoSuchElementException {
    public RoleNotFoundException(Long userId) {
        super("No role with such ID: " + userId);
    }
}