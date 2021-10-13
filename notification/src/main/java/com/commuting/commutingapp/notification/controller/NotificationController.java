package com.commuting.commutingapp.notification.controller;

import com.commuting.commutingapp.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping()
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(notificationService.get());
    }

}
