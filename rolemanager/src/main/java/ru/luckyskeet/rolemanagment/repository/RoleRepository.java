package ru.luckyskeet.rolemanagment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.luckyskeet.rolemanagment.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
