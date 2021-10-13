package com.commuting.commutingapp.trip.service.api.impl;

import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.position.dto.PositionDto;
import com.commuting.commutingapp.position.service.PositionService;
import com.commuting.commutingapp.trip.model.Passenger;
import com.commuting.commutingapp.trip.model.Stop;
import com.commuting.commutingapp.trip.model.Trip;
import com.commuting.commutingapp.trip.repo.TripRepo;
import com.commuting.commutingapp.trip.service.api.TripPositionService;
import com.commuting.commutingapp.trip.utils.TripPermissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class TripPositionServiceImpl implements TripPositionService {

    @Autowired
    PositionService positionService;

    @Autowired
    TripRepo tripRepo;

    @Override
    public PositionDto get(String tripId, String userId) {
        String currentUserId = SecurityUtils.getUserId();
        Trip trip = tripRepo.findById(tripId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip " + tripId + " not found!"));
        Set<Passenger> passengers = trip.getPassengers();

        if (TripPermissionUtils.isCurrentUserPassenger(currentUserId, passengers) &&
                TripPermissionUtils.isDriver(userId, trip) &&
                TripPermissionUtils.isActiveTrip(trip)
        ) {
            return positionService.get(userId);
        }

        Stop pickup = trip.getStops().stream().filter(s -> s.isPickup() && s.getUserId().equals(userId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pick up not found for trip " + trip.getId()));
        Stop drop = trip.getStops().stream().filter(s -> !s.isPickup() && s.getUserId().equals(userId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Drop not found for trip " + trip.getId()));

        if ((TripPermissionUtils.isAlmostPickupOrRiding(pickup) ||
                !drop.isConfirmed()) &&
                TripPermissionUtils.isDriver(currentUserId, trip)) {
            return positionService.get(userId);
        }

        return null;
    }

}
