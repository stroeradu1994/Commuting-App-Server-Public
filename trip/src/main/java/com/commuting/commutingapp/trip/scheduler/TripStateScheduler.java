package com.commuting.commutingapp.trip.scheduler;

import com.commuting.commutingapp.common.utils.DateTimeUtils;
import com.commuting.commutingapp.notification.model.Notification;
import com.commuting.commutingapp.notification.model.NotificationType;
import com.commuting.commutingapp.notification.service.NotificationService;
import com.commuting.commutingapp.trip.model.TripState;
import com.commuting.commutingapp.trip.model.TripStatus;
import com.commuting.commutingapp.trip.repo.TripRepo;
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
public class TripStateScheduler {

    @Autowired
    TripRepo tripRepo;

    @Autowired
    NotificationService notificationService;

    Map<String, LocalDateTime> confirmTripTracker = new HashMap<>();
    Map<String, LocalDateTime> startIn10TripTracker = new HashMap<>();
    Map<String, LocalDateTime> startNowTripTracker = new HashMap<>();

    @Scheduled(fixedDelay = 60000) // every minute
    @Transactional
    public void tripStateScheduler() {

        // notify unconfirmed for confirmation
        tripRepo.findAllByStatusAndLeaveAtLessThan(TripStatus.IDLE, DateTimeUtils.getTimeNow().minusMinutes(30)).stream()
                .filter(trip -> !confirmTripTracker.containsKey(trip.getId()))
                .forEach(trip -> {
                    confirmTripTracker.put(trip.getId(), DateTimeUtils.getTimeNow());
                        notificationService.sendNotification(new Notification(trip.getDriverId(), NotificationType.PROMPT_TO_CONFIRM, trip.getId()));
                });

        tripRepo.findAllByStatusAndLeaveAtLessThan(TripStatus.IDLE, DateTimeUtils.getTimeNow()).stream()
                .forEach(trip -> {
                    trip.setStatus(TripStatus.EXPIRED);
                    trip.setState(TripState.PAST);
                    tripRepo.save(trip);
                    notificationService.sendNotification(new Notification(trip.getDriverId(), NotificationType.TRIP_EXPIRED, trip.getId()));
                    trip.getPassengers().forEach(passenger -> {
                        notificationService.sendNotification(new Notification(passenger.getUserId(), NotificationType.TRIP_EXPIRED, trip.getId()));
                    });
                });

        // notify
        tripRepo.findAllByStatusAndLeaveAtLessThan(TripStatus.CONFIRMED, DateTimeUtils.getTimeNow().minusMinutes(10))
                .stream()
                .filter(trip -> !startIn10TripTracker.containsKey(trip.getId()))
                .forEach(trip -> {
                    startIn10TripTracker.put(trip.getId(), DateTimeUtils.getTimeNow());
                    notificationService.sendNotification(new Notification(trip.getDriverId(), NotificationType.TRIP_STARTING_IN_10, trip.getId()));
                    trip.getPassengers().forEach(passenger -> {
                        notificationService.sendNotification(new Notification(passenger.getUserId(), NotificationType.TRIP_STARTING_IN_10, trip.getId()));
                    });
                });

        tripRepo.findAllByStatusAndLeaveAtLessThan(TripStatus.CONFIRMED, DateTimeUtils.getTimeNow())
                .stream()
                .filter(trip -> !startNowTripTracker.containsKey(trip.getId()))
                .forEach(trip -> {
                    startNowTripTracker.put(trip.getId(), DateTimeUtils.getTimeNow());
                    notificationService.sendNotification(new Notification(trip.getDriverId(), NotificationType.PROMPT_TO_START, trip.getId()));
                });

        tripRepo.findAllByStatusAndLeaveAtLessThan(TripStatus.CONFIRMED, DateTimeUtils.getTimeNow().plusMinutes(10))
                .forEach(trip -> {
                    trip.setStatus(TripStatus.EXPIRED);
                    trip.setState(TripState.PAST);
                    tripRepo.save(trip);
                    notificationService.sendNotification(new Notification(trip.getDriverId(), NotificationType.TRIP_EXPIRED, trip.getId()));
                    trip.getPassengers().forEach(passenger -> {
                        notificationService.sendNotification(new Notification(passenger.getUserId(), NotificationType.TRIP_EXPIRED, trip.getId()));
                    });
                });

        clearTracker(confirmTripTracker, 30);
        clearTracker(startIn10TripTracker, 30);
        clearTracker(startNowTripTracker, 30);
    }

    void clearTracker(Map<String, LocalDateTime> tracker, int expireAfterMinutes) {
        List<String> keysToRemove = tracker.entrySet().stream().filter((entry) -> entry.getValue().plusMinutes(expireAfterMinutes).isAfter(DateTimeUtils.getTimeNow())).map(Map.Entry::getKey).collect(Collectors.toList());

        keysToRemove.forEach(tracker::remove);
    }
}
