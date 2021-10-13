package com.commuting.commutingapp.notification.repo;

import com.commuting.commutingapp.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, String> {

    Page<Notification> findAllByUserId(String userId, Pageable pageable);
}