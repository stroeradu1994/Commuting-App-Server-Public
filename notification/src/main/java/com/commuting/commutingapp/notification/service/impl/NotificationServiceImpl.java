package com.commuting.commutingapp.notification.service.impl;

import com.commuting.commutingapp.common.utils.DateTimeUtils;
import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.notification.model.Notification;
import com.commuting.commutingapp.notification.repo.NotificationRepo;
import com.commuting.commutingapp.notification.service.NotificationService;
import com.commuting.commutingapp.security.service.AccountService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepo notificationRepo;

    @Autowired
    AccountService accountService;

    @Override
    public void sendNotification(Notification notification) {
        notification.setCreatedAt(DateTimeUtils.getTimeNow());
        notification.setText(notification.getType().getLabel());
        notificationRepo.save(notification);

        Message message = Message.builder()
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle("Commuting")
                        .setBody(notification.getType().getLabel())
                        .build())
                .putData("entity", notification.getEntityId())
                .putData("type", notification.getType().toString())
                .setToken(accountService.getInternalAccount(notification.getUserId()).getFcmToken())
                .build();
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Notification> get() {
        Pageable pageable = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "createdAt"));
        return notificationRepo.findAllByUserId(SecurityUtils.getUserId(), pageable).getContent();
    }
}
