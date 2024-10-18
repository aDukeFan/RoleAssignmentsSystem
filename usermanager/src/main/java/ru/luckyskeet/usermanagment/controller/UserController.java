package ru.luckyskeet.usermanagment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.luckyskeet.usermanagment.documentation.UserApiDoc;
import ru.luckyskeet.usermanagment.model.UserDtoRequest;
import ru.luckyskeet.usermanagment.model.UserDtoResponse;
import ru.luckyskeet.usermanagment.model.UserDtoToUpdate;
import ru.luckyskeet.usermanagment.security.CheckAccess;
import ru.luckyskeet.usermanagment.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users")
@AllArgsConstructor
@Validated
@Slf4j
public class UserController implements UserApiDoc {

    private final UserService service;

    @GetMapping
    public List<UserDtoResponse> getUsers(@RequestParam(defaultValue = "0", required = false) @Min(0) Integer from,
                                          @RequestParam(defaultValue = "10", required = false) @Min(0) @Max(50) Integer size) {
        log.info("GET /api/users called with from={} and size={}", from, size);
        return service.getUsers(from, size);
    }

    @GetMapping("/{userId}")
    public UserDtoResponse getUserById(@PathVariable @Min(1) Long userId) {
        log.info("GET /api/users/{} called", userId);
        return service.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDtoResponse createUser(@Valid @RequestBody UserDtoRequest userDtoRequest) {
        log.info("POST /api/users called with {}", userDtoRequest);
        return service.createUser(userDtoRequest);
    }

    @PutMapping("/{userId}")
    @CheckAccess
    public UserDtoResponse updateUser(@PathVariable @Min(1) Long userId,
                                      @Valid @RequestBody UserDtoToUpdate userDtoToUpdate) {
        log.info("PUT /api/users/{} called with {}", userId, userDtoToUpdate);
        return service.updateUserById(userId, userDtoToUpdate);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckAccess
    public void deleteUserById(@PathVariable @Min(1) Long userId) {
        log.info("DELETE /api/users/{} called", userId);
        service.deleteUserById(userId);
    }
}
