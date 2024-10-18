package ru.luckyskeet.coordinator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "role_ids", schema = "public")
@Getter
@Setter
@Accessors(chain = true)
public class RoleId {
    @Id
    private Long id;
}