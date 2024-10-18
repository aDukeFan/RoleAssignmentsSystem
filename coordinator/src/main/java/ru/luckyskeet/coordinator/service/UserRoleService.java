package ru.luckyskeet.coordinator.service;

import ru.luckyskeet.coordinator.model.UserRole;

import java.util.List;

public interface UserRoleService {

    List<Long> getUserRoles(Long userId);

    UserRole setRoleToUser(Long userId, Long roleId);

    void deleteRoleFromUser(Long userId, Long roleId);
}
