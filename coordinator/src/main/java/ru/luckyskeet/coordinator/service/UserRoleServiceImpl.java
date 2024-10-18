package ru.luckyskeet.coordinator.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.luckyskeet.coordinator.model.UserRole;
import ru.luckyskeet.coordinator.repository.UserRoleRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository repository;

    @Override
    public List<Long> getUserRoles(Long userId) {
        log.info("Fetching roles for user with ID: {}", userId);
        List<Long> roles = repository.findRolesIdsByUserId(userId);
        log.info("Roles fetched for user ID {}: {}", userId, roles);
        return roles;
    }

    @Override
    public UserRole setRoleToUser(Long userId, Long roleId) {
        log.info("Setting role ID {} to user ID {}", roleId, userId);
        UserRole userRole = repository.save(new UserRole(userId, roleId));
        log.info("Role set successfully: {}", userRole);
        return userRole;
    }

    @Override
    public void deleteRoleFromUser(Long userId, Long roleId) {
        log.info("Deleting role ID {} from user ID {}", roleId, userId);
        repository.delete(new UserRole(userId, roleId));
        log.info("Role ID {} deleted from user ID {}", roleId, userId);
    }
}
