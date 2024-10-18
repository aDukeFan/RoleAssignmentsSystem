package ru.luckyskeet.coordinator.controller;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.luckyskeet.coordinator.documentation.UserRoleApiDoc;
import ru.luckyskeet.coordinator.model.UserRole;
import ru.luckyskeet.coordinator.service.UserRoleService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users")
@AllArgsConstructor
@Slf4j
@Validated
public class CoordinatorController implements UserRoleApiDoc {

    private final UserRoleService service;

    @GetMapping("/{userId}/roles")
    public List<Long> getUserRoles(@PathVariable @Min(1) Long userId) {
        log.info("GET /api/users/{}/roles called", userId);
        return service.getUserRoles(userId);
    }

    @PostMapping("/{userId}/roles/{roleId}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRole setRoleToUserById(@PathVariable @Min(1) Long userId,
                                      @PathVariable @Min(1) Long roleId) {
        log.info("POST /api/users/{}/roles/{} called", userId, roleId);
        return service.setRoleToUser(userId, roleId);
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoleOfUserById(@PathVariable @Min(1) Long userId,
                                     @PathVariable @Min(1) Long roleId) {
        log.info("DELETE /api/users/{}/roles/{} called", userId, roleId);
        service.deleteRoleFromUser(userId, roleId);
    }
}

