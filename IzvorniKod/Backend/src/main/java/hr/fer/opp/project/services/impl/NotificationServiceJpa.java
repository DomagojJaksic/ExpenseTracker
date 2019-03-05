package hr.fer.opp.project.services.impl;

import hr.fer.opp.project.entities.Notification;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.entities.complexEntities.NotifiedUser;
import hr.fer.opp.project.repos.NotificationRepository;
import hr.fer.opp.project.repos.NotifiedUserRepository;
import hr.fer.opp.project.services.EntityMissingException;
import hr.fer.opp.project.services.NotificationService;
import hr.fer.opp.project.services.RequestDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceJpa implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepo;

    @Autowired
    private NotifiedUserRepository notifiedUserRepo;

    @Override
    public Notification fetchNotification(long id) {
        Optional<Notification> notification = findById(id);
        if(notification.isPresent()) {
            return notification.get();
        }
        throw new EntityMissingException(Notification.class, id);
    }

    @Override
    public List<Notification> fetchByUser(User user) {
        Optional<List<NotifiedUser>> notifiedUsers = notifiedUserRepo.findByUser(user);
        if(notifiedUsers.isPresent()) {
            List<NotifiedUser> notifiedUserList = notifiedUsers.get();
            List<Notification> notifications = new ArrayList<>();
            for(NotifiedUser notifiedUser : notifiedUserList) {
                Notification notification = fetchNotification(notifiedUser.getNotification().getNotificationID());
                if(user.getLastHomeGroupMembershipChangeTime().isBefore(notification.getNotificationTime()))
                notifications.add(notification);
            }
            return notifications;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<User> fetchNotifiedUsers(Notification notification) {
        Optional<List<NotifiedUser>> notifiedUsers = notifiedUserRepo.findByNotification(notification);
        if(notifiedUsers.isPresent()) {
            List<NotifiedUser> notifiedUserList = notifiedUsers.get();
            List<User> users = new ArrayList<>();
            for(NotifiedUser notifiedUser : notifiedUserList) {
                users.add(notifiedUser.getUser());
            }
            return users;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Notification> findById(long id) {
        return notificationRepo.findById(id);
    }

    @Override
    public Notification createNotification(Notification notification) {
        validate(notification);
        return notificationRepo.save(notification);
    }

    @Override
    public Notification updateNotification(Notification notification) {
        validate(notification);
        return notificationRepo.save(notification);
    }

    @Override
    public Notification deleteNotification(long id) {
        Notification notification = fetchNotification(id);
        notificationRepo.delete(notification);
        return notification;
    }

    @Override
    public NotifiedUser fetchNotifiedUser(long id) {
        Optional<NotifiedUser> notifiedUser = notifiedUserRepo.findById(id);
        if(notifiedUser.isPresent()) {
            return notifiedUser.get();
        } else {
            throw new EntityMissingException(NotifiedUser.class, id);
        }
    }

    @Override
    public NotifiedUser createNotifiedUser(NotifiedUser notifiedUser) {
        validateAddingNotifiedUser(notifiedUser);
        return notifiedUserRepo.save(notifiedUser);
    }

    @Override
    public NotifiedUser updateNotifiedUser(NotifiedUser notifiedUser) {
        if (!notifiedUserRepo.existsById(notifiedUser.getNotifiedUserID())) {
            throw new EntityMissingException(User.class, notifiedUser.getNotifiedUserID());
        }
        return notifiedUserRepo.save(notifiedUser);
    }

    @Override
    public NotifiedUser deleteNotifiedUser(long id) {
        NotifiedUser notifiedUser = fetchNotifiedUser(id);
        notifiedUserRepo.delete(notifiedUser);
        return notifiedUser;
    }

    private void validate(Notification notification) {
        Assert.notNull(notification, "Notification must not be null");
        Assert.notNull(notification.getNotificationMessage(), "Notification must not be null");
        Assert.notNull(notification.getNotificationType(), "Notification type must not be null");
        Assert.notNull(notification.getNotificationTime(), "Notification time must not be null");
    }

    private void validateAddingNotifiedUser(NotifiedUser notifiedUser) {
        List<Notification> notifications = fetchByUser(notifiedUser.getUser());
        for(Notification notification : notifications) {
            if(notification.equals(notifiedUser.getNotification())) {
                throw new RequestDeniedException("User is already notified!");
            }
        }
    }
}
