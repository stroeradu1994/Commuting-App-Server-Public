package com.commuting.commutingapp.trip.scheduler;

import com.commuting.commutingapp.common.utils.DateTimeUtils;
import com.commuting.commutingapp.notification.model.Notification;
import com.commuting.commutingapp.notification.model.NotificationType;
import com.commuting.commutingapp.notification.service.NotificationService;
import com.commuting.commutingapp.trip.repo.TripRequestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TripRequestStateScheduler {

    @Autowired
    TripRequestRepo tripRequestRepo;

    @Autowired
    NotificationService notificationService;

    Map<String, LocalDateTime> tripRequestReminder = new HashMap<>();

    @Scheduled(fixedDelay = 60000) // every minute
    @Transactional
    public void tripStateScheduler() {
//        tripRequestRepo.findAllByConfirmedAndArriveByLessThan(false, DateTimeUtils.getTimeNow().plusMinutes(30))
//                .forEach(tripRequest -> {
//                    tripRequestRepo.deleteById(tripRequest.getId());
//                    notificationService.sendNotification(new Notification(tripRequest.getUserId(), NotificationType.TRIP_REQUEST_DELETED, tripRequest.getId()));
//                });

//        tripRequestRepo.findAllByConfirmedAndArriveByLessThan(false, DateTimeUtils.getTimeNow().minusMinutes(30)).stream()
//                .filter(tripRequest -> !tripRequestReminder.containsKey(tripRequest.getId()))
//                .forEach(tripRequest -> {
//                    tripRequestReminder.put(tripRequest.getId(), DateTimeUtils.getTimeNow());
//                    notificationService.sendNotification(new Notification(tripRequest.getUserId(), NotificationType.TRIP_REQUEST_REMINDER, tripRequest.getId()));
//                });

//        clearTracker(tripRequestReminder, 30);
    }

    void clearTracker(Map<String, LocalDateTime> tracker, int expireAfterMinutes) {
        List<String> keysToRemove = tracker.entrySet().stream().filter((entry) -> entry.getValue().plusMinutes(expireAfterMinutes).isAfter(DateTimeUtils.getTimeNow())).map(Map.Entry::getKey).collect(Collectors.toList());

        keysToRemove.forEach(tracker::remove);
    }
}
