package com.commuting.commutingapp.trip.service.api.impl;

import com.commuting.commutingapp.common.utils.LocationUtils;
import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.direction.service.api.AddressService;
import com.commuting.commutingapp.notification.model.Notification;
import com.commuting.commutingapp.notification.model.NotificationType;
import com.commuting.commutingapp.notification.service.NotificationService;
import com.commuting.commutingapp.trip.model.*;
import com.commuting.commutingapp.trip.repo.*;
import com.commuting.commutingapp.trip.service.api.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    MatchRepo matchRepo;

    @Autowired
    StopRepo stopRepo;

    @Autowired
    TripRepo tripRepo;

    @Autowired
    PassengerRepo passengerRepo;

    @Autowired
    NotificationService notificationService;

    @Autowired
    TripRequestRepo tripRequestRepo;

    @Autowired
    AddressService addressService;

    @Override
    @Transactional
    public void match(String id) {
        Match match = matchRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match " + id + " not found!"));

        String userId = SecurityUtils.getUserId();

        if (!match.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permission to perform this action");
        }

        TripRequest tripRequest = match.getTripRequest();
        Trip trip = match.getTrip();

        Stop pickupStop = new Stop(match.getPickupPoint(), addressService.getAddress(LocationUtils.locationFromString(match.getPickupPoint())), match.getPickupWalkingDistance(), true, userId, trip, match.getPickupTime());
        Stop dropStop = new Stop(match.getDropPoint(), addressService.getAddress(LocationUtils.locationFromString(match.getDropPoint())), match.getDropWalkingDistance(), false, userId, trip, match.getDropTime());

        stopRepo.save(pickupStop);
        stopRepo.save(dropStop);

        Passenger passenger = new Passenger(userId, trip, match.getLeaveTime(), match.getArriveTime(), match.getTripDistance(), tripRequest.getFrom(), tripRequest.getTo());

        passengerRepo.save(passenger);

        match.setConfirmed(true);
        tripRequest.setConfirmed(true);
        tripRequestRepo.save(tripRequest);
        matchRepo.save(match);

        notificationService.sendNotification(new Notification(trip.getDriverId(), NotificationType.PASSENGER_JOIN, trip.getId()));
    }

    @Override
    @Transactional
    public void unmatch(String id) {
        String userId = SecurityUtils.getUserId();
        Match match = matchRepo.findByTripIdAndUserId(id, userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match " + id + " not found!"));
        TripRequest tripRequest = match.getTripRequest();
        Trip trip = match.getTrip();
        Passenger passenger = trip.getPassengers().stream().filter(p -> p.getUserId().equals(userId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger " + userId + " not found!"));

        Stop pickup = trip.getStops().stream().filter(stop -> stop.getUserId().equals(userId)).filter(Stop::isPickup).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pickup not found!"));
        Stop drop = trip.getStops().stream().filter(stop -> stop.getUserId().equals(userId)).filter(stop -> !stop.isPickup()).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Drop not found!"));

        trip.removeMatch(match);
        trip.removePassenger(passenger);
        trip.removeStop(pickup);
        trip.removeStop(drop);
        tripRequest.setConfirmed(false);
        match.setConfirmed(false);

        tripRequestRepo.save(tripRequest);
        matchRepo.save(match);
        tripRepo.save(trip);
        passengerRepo.delete(passenger);
        stopRepo.delete(pickup);
        stopRepo.delete(drop);

        notificationService.sendNotification(new Notification(trip.getDriverId(), NotificationType.PASSENGER_LEFT, trip.getId()));
    }
}
