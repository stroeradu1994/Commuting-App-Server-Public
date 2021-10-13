package com.commuting.commutingapp.trip.service.api.impl;

import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.profile.model.Profile;
import com.commuting.commutingapp.profile.service.ProfileService;
import com.commuting.commutingapp.trip.dto.statistic.RatingRequest;
import com.commuting.commutingapp.trip.model.Passenger;
import com.commuting.commutingapp.trip.model.Trip;
import com.commuting.commutingapp.trip.model.TripState;
import com.commuting.commutingapp.trip.repo.PassengerRepo;
import com.commuting.commutingapp.trip.repo.TripRepo;
import com.commuting.commutingapp.trip.service.api.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    TripRepo tripRepo;

    @Autowired
    PassengerRepo passengerRepo;

    @Autowired
    ProfileService profileService;

    @Override
    public void rate(RatingRequest ratingRequest) {
        String userId = SecurityUtils.getUserId();

        Trip trip = tripRepo.findById(ratingRequest.getTripId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip " + ratingRequest.getTripId() + " not found!"));
        Passenger passenger = getPassenger(trip, userId);
        Profile driverProfile = profileService.getById(trip.getDriverId());

        passenger.setRating(ratingRequest.getRate());
        passengerRepo.save(passenger);

        driverProfile.addRating(ratingRequest.getRate());
        profileService.createUpdateProfile(driverProfile);
    }

    private Passenger getPassenger(Trip trip, String passengerId) {
        return trip.getPassengers().stream().filter(p -> p.getUserId().equals(passengerId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger not found!"));
    }

}
