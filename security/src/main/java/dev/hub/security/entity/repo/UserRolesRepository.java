package dev.hub.security.entity.repo;

import dev.hub.security.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {
    Optional<UserRoles> findByUserId(String userId);
}
