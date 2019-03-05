package hr.fer.opp.project.controllers;

import hr.fer.opp.project.entities.complexEntities.NotifiedUser;
import hr.fer.opp.project.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/notificationmembers")
public class NotificationMemberController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public NotifiedUser getNotifiedUser(@PathVariable("id") long id) {
        return notificationService.fetchNotifiedUser(id);
    }

    @PostMapping("")
    @Secured("ROLE_USER")
    public ResponseEntity<NotifiedUser> createNotifiedUser(@RequestBody NotifiedUser notifiedUser) {
        NotifiedUser saved = notificationService.createNotifiedUser(notifiedUser);
        return ResponseEntity.created(URI.create("/notifications/" + saved.getNotification().getNotificationID()
                + "/members" )).body(saved);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_USER")
    public NotifiedUser updateNotifiedUser(@PathVariable("id") long id,
                                           @RequestBody NotifiedUser notifiedUser) {
        if (!notifiedUser.getNotifiedUserID().equals(id)) {
            throw new IllegalArgumentException("NotifiedUserID must be preserved");
        }
        return notificationService.updateNotifiedUser(notifiedUser);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_USER")
    public NotifiedUser deleteNotifiedUser(@PathVariable("id") long id) {
        return notificationService.deleteNotifiedUser(id);
    }
}
