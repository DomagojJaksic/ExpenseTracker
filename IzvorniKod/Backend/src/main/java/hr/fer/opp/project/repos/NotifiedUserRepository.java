package hr.fer.opp.project.repos;

import hr.fer.opp.project.entities.Notification;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.entities.complexEntities.NotifiedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotifiedUserRepository extends JpaRepository<NotifiedUser, Long> {
    Optional<List<NotifiedUser>> findByUser(User user);
    Optional<List<NotifiedUser>> findByNotification(Notification notification);
}
