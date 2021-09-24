package de.hexad.libmanagement.model.repository;

import de.hexad.libmanagement.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
