package cz.upce.project.repository;

import cz.upce.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByResetPasswordToken(String resetPasswordToken);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
