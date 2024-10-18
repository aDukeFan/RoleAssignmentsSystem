package ru.luckyskeet.coordinator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.luckyskeet.coordinator.model.RoleId;

@Repository
public interface RoleIdRepository extends JpaRepository<RoleId, Long> {
}
