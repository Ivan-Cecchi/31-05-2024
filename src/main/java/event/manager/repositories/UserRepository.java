package event.manager.repositories;

import event.manager.entities.User;
import event.manager.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);


    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameOrEmail(String username, String email);

    boolean existsByUsernameAndEmail(String username, String email);


    boolean existsByRole(UserRole role);

    int countByRole(UserRole role);
}
