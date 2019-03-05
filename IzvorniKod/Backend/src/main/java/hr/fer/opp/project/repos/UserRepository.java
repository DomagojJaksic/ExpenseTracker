package hr.fer.opp.project.repos;

import hr.fer.opp.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    int countByUsername(String username);
    int countByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByUsernameAndUserIDNot(String username, Long userID);
    Optional<User> findByEmail(String email);
}
