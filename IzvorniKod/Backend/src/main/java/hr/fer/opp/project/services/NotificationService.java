package hr.fer.opp.project.services;

import hr.fer.opp.project.entities.Notification;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.entities.complexEntities.NotifiedUser;

import java.util.List;
import java.util.Optional;

public interface NotificationService {

    Notification fetchNotification(long id);

    List<Notification> fetchByUser(User user);

    List<User> fetchNotifiedUsers(Notification notification);

    Optional<Notification> findById(long id);

    Notification createNotification(Notification notification);

    Notification updateNotification(Notification notification);

    Notification deleteNotification(long id);

    NotifiedUser fetchNotifiedUser(long id);

    NotifiedUser createNotifiedUser(NotifiedUser notifiedUser);

    NotifiedUser updateNotifiedUser(NotifiedUser notifiedUser);

    NotifiedUser deleteNotifiedUser(long id);
}
