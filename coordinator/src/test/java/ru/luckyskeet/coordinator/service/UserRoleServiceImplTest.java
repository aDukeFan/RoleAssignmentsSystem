package ru.luckyskeet.coordinator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.luckyskeet.coordinator.model.UserRole;
import ru.luckyskeet.coordinator.repository.UserRoleRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceImplTest {

    @Mock
    private UserRoleRepository repository;

    @InjectMocks
    private UserRoleServiceImpl userRoleService;

    private Long userId;
    private Long roleId;
    private UserRole userRole;

    @BeforeEach
    void setUp() {
        userId = 1L;
        roleId = 2L;
        userRole = new UserRole(userId, roleId);
    }

    @Test
    void getUserRoles_shouldReturnRoles() {
        List<Long> expectedRoles = Arrays.asList(1L, 2L);

        when(repository.findRolesIdsByUserId(userId)).thenReturn(expectedRoles);

        List<Long> actualRoles = userRoleService.getUserRoles(userId);

        assertThat(actualRoles).isEqualTo(expectedRoles);
        verify(repository).findRolesIdsByUserId(userId);
    }

    @Test
    void setRoleToUser_shouldSaveRole() {
        when(repository.save(any(UserRole.class))).thenReturn(userRole);

        UserRole actualUserRole = userRoleService.setRoleToUser(userId, roleId);

        assertThat(actualUserRole).isEqualTo(userRole);
        verify(repository).save(any(UserRole.class));
    }

    @Test
    void deleteRoleFromUser_shouldDeleteRole() {
        userRoleService.deleteRoleFromUser(userId, roleId);
        verify(repository).delete(userRole);
    }
}
