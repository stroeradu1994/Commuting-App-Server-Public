package com.commuting.commutingapp.notification.model;

import com.commuting.commutingapp.common.utils.DateTimeUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
public class Notification {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "notification_id", updatable = false, nullable = false)
    private String id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String text;

    private String entityId;

    private LocalDateTime createdAt;

    public Notification() {
    }

    public Notification(String userId, NotificationType type, String entityId) {
        this.userId = userId;
        this.type = type;
        this.entityId = entityId;
        this.createdAt = DateTimeUtils.getTimeNow();
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
