package ru.luckyskeet.coordinator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.luckyskeet.coordinator.model.UserId;

@Repository
public interface UserIdRepository extends JpaRepository<UserId, Long> {
}