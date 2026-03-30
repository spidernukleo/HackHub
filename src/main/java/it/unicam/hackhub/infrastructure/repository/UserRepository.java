package it.unicam.hackhub.infrastructure.repository;


import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.domain.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    List<User> findByUserRole(UserRole userRole);
}
