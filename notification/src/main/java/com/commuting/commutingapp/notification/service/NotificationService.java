package com.commuting.commutingapp.notification.service;

import com.commuting.commutingapp.notification.model.Notification;

import java.util.List;

public interface NotificationService {
    void sendNotification(Notification notification);

    List<Notification> get();
}
