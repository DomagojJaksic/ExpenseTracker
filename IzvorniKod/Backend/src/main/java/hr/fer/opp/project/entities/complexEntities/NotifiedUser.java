package hr.fer.opp.project.entities.complexEntities;

import hr.fer.opp.project.entities.Notification;
import hr.fer.opp.project.entities.User;

import javax.persistence.*;

@Entity
public class NotifiedUser {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notifiedUserID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;

    public NotifiedUser() {}

    public NotifiedUser(User user, Notification notification) {
        this.user = user;
        this.notification = notification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotifiedUser)) return false;

        NotifiedUser that = (NotifiedUser) o;

        return notifiedUserID != null ? notifiedUserID.equals(that.notifiedUserID) : that.notifiedUserID == null;
    }

    @Override
    public int hashCode() {
        return notifiedUserID != null ? notifiedUserID.hashCode() : 0;
    }

    /**
     * Gets notification.
     *
     * @return Value of notification.
     */
    public Notification getNotification() {
        return notification;
    }

    /**
     * Sets new notifiedUsers.
     *
     * @param notifiedUsers New value of notifiedUsers.
     */
    public void setUser(User notifiedUsers) {
        this.user = notifiedUsers;
    }

    /**
     * Gets notifiedUsers.
     *
     * @return Value of notifiedUsers.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets new notification.
     *
     * @param notification New value of notification.
     */
    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    /**
     * Gets notifiedUserID.
     *
     * @return Value of notifiedUserID.
     */
    public Long getNotifiedUserID() {
        return notifiedUserID;
    }
}
