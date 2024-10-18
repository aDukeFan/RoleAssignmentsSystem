package ru.luckyskeet.coordinator.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_roles")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    public UserRole(Long userId, Long roleId) {
        this.id = new UserRoleId(userId, roleId);
    }
}
