package hr.fer.opp.project.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import hr.fer.opp.project.enums.NotificationType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
public class Notification {

    /**
     * Notification ID
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationID;

    /**
     * Notification typeq
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private NotificationType notificationType;

    /**
     * Notification message
     */
    @Column
    @NotNull
    private String notificationMessage;


    /**
     * Notification time
     */
    @Column
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @NotNull
    private LocalDateTime notificationTime;


    public Notification() {};

    public Notification(@NotNull NotificationType notificationType,
                        @NotNull String notificationMessage,
                        @NotNull LocalDateTime notificationTime) {
        this.notificationType = notificationType;
        this.notificationMessage = notificationMessage;
        this.notificationTime = notificationTime;
    }

    /**
     * Sets new Notification time.
     *
     * @param notificationTime New value of Notification time.
     */
    public void setNotificationTime(LocalDateTime notificationTime) {
        this.notificationTime = notificationTime;
    }

    /**
     * Gets Notification type.
     *
     * @return Value of Notification type.
     */
    public NotificationType getNotificationType() {
        return notificationType;
    }

    /**
     * Sets new Notification type.
     *
     * @param notificationType New value of Notification type.
     */
    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    /**
     * Sets new Notification message.
     *
     * @param notificationMessage New value of Notification message.
     */
    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    /**
     * Gets Notification message.
     *
     * @return Value of Notification message.
     */
    public String getNotificationMessage() {
        return notificationMessage;
    }

    /**
     * Gets Notification time.
     *
     * @return Value of Notification time.
     */
    public LocalDateTime getNotificationTime() {
        return notificationTime;
    }

    /**
     * Gets Notification ID.
     *
     * @return Value of Notification ID.
     */
    public Long getNotificationID() {
        return notificationID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;

        Notification that = (Notification) o;

        return notificationID != null ? notificationID.equals(that.notificationID) : that.notificationID == null;
    }

    @Override
    public int hashCode() {
        return notificationID != null ? notificationID.hashCode() : 0;
    }
}
