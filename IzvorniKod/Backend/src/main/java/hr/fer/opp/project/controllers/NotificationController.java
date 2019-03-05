package hr.fer.opp.project.controllers;


import hr.fer.opp.project.entities.Notification;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.entities.complexEntities.NotifiedUser;
import hr.fer.opp.project.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("{id}")
    @Secured("ROLE_USER")
    public Notification fetchNotification(@PathVariable("id") long id) {
        return notificationService.fetchNotification(id);
    }

    @PostMapping("")
    @Secured("ROLE_USER")
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification saved = notificationService.createNotification(notification);
        return ResponseEntity.created(URI.create("/notifications/" + saved.getNotificationID())).body(saved);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_USER")
    public Notification updateNotification(@PathVariable("id") long id, @RequestBody Notification notification) {
        if (!notification.getNotificationID().equals(id)) {
            throw new IllegalArgumentException("NotificationID must be preserved");
        }
        return notificationService.updateNotification(notification);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_USER")
    public Notification deleteNotification(@PathVariable("id") long id) {
        return notificationService.deleteNotification(id);
    }

    @GetMapping("{id}/notificationmembers")
    @Secured("ROLE_USER")
    public List<User> getNotifiedUsers(@PathVariable("id") long id) {
        Notification notification = fetchNotification(id);
        return notificationService.fetchNotifiedUsers(notification);
    }
}
