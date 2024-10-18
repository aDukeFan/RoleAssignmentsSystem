package ru.luckyskeet.coordinator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.luckyskeet.coordinator.model.UserRole;
import ru.luckyskeet.coordinator.model.UserRoleId;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    @Query(value = "SELECT role_id FROM user_roles WHERE user_id = ?",
            nativeQuery = true)
    List<Long> findRolesIdsByUserId(Long userId);
}