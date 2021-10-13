package com.commuting.commutingapp.trip.service.api.impl;

import com.commuting.commutingapp.common.utils.DateTimeUtils;
import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.trip.dto.trip.TripAvailabilityRequest;
import com.commuting.commutingapp.trip.model.TripState;
import com.commuting.commutingapp.trip.repo.TripRepo;
import com.commuting.commutingapp.trip.repo.TripRequestRepo;
import com.commuting.commutingapp.trip.service.api.TripAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class TripAvailabilityServiceImpl implements TripAvailabilityService {

    @Autowired
    TripRepo tripRepo;

    @Autowired
    TripRequestRepo tripRequestRepo;

    @Override
    public void checkAvailability(TripAvailabilityRequest tripAvailabilityRequest) {

        LocalDateTime dateTime = LocalDateTime.parse(tripAvailabilityRequest.getDateTime());
        String userId = SecurityUtils.getUserId();

        LocalDateTime leaveAt = tripAvailabilityRequest.isDriver() ?
                dateTime :
                tripAvailabilityRequest.isAsap() ?
                        DateTimeUtils.getTimeNow() :
                        dateTime.minusMinutes(120);

        LocalDateTime arriveAt = tripAvailabilityRequest.isDriver() ?
                dateTime.plusMinutes(60) :
                tripAvailabilityRequest.isAsap() ?
                        DateTimeUtils.getTimeNow().plusMinutes(120) :
                        dateTime;

        if (hasConflictByTrips(userId, leaveAt.minusMinutes(60), arriveAt.plusMinutes(60)) ||
                hasConflictByTripRequests(userId, leaveAt.minusMinutes(60), arriveAt.plusMinutes(60)))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conflict cu alte curse");
    }

    private boolean hasConflictByTrips(String userId, LocalDateTime leaveAt, LocalDateTime arriveAt) {
        return tripRepo.existsByStateAndDriverIdAndLeaveAtGreaterThanAndArriveAtLessThan(TripState.UPCOMING, userId, leaveAt, arriveAt) ||
                tripRepo.existsByStateAndDriverIdAndLeaveAtGreaterThanAndArriveAtLessThan(TripState.ACTIVE, userId, leaveAt, arriveAt) ||
                tripRepo.existsByStateAndPassengersUserIdAndLeaveAtGreaterThanAndArriveAtLessThan(TripState.UPCOMING, userId, leaveAt, arriveAt) ||
                tripRepo.existsByStateAndPassengersUserIdAndLeaveAtGreaterThanAndArriveAtLessThan(TripState.ACTIVE, userId, leaveAt, arriveAt);
    }

    private boolean hasConflictByTripRequests(String userId, LocalDateTime leaveAt, LocalDateTime arriveAt) {
        return tripRequestRepo.existsByUserIdAndConfirmedAndArriveByGreaterThanAndArriveByLessThan(userId, false, leaveAt, arriveAt);
    }
}
