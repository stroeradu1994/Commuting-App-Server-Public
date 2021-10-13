package com.commuting.commutingapp.trip.service.mapper.impl;

import com.commuting.commutingapp.profile.dto.response.CarResponse;
import com.commuting.commutingapp.profile.model.Profile;
import com.commuting.commutingapp.profile.service.CarService;
import com.commuting.commutingapp.profile.service.ProfileService;
import com.commuting.commutingapp.trip.dto.trip.common.MatchResponse;
import com.commuting.commutingapp.trip.model.Match;
import com.commuting.commutingapp.trip.model.Trip;
import com.commuting.commutingapp.trip.service.mapper.MatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchMapperImpl implements MatchMapper {

    @Autowired
    ProfileService profileService;

    @Autowired
    CarService carService;

    public MatchResponse buildTripResponse(Match match) {

        Trip trip = match.getTrip();
        String driverId = trip.getDriverId();
        String carId = trip.getCarId();

        Profile profile = profileService.getById(driverId);
        CarResponse carResponse = carService.getCar(carId);

        return new MatchResponse(
                match.getId(),
                profile.getId(),
                profile.getFirstName() + ' ' + profile.getLastName(),
                trip.getId(),
                carResponse.getBrand(),
                carResponse.getModel(),
                carResponse.getColor(),
                profile.getRating(),
                trip.getPath(),
                match.getPickupWalkingDistance(),
                match.getDropWalkingDistance(),
                match.getPickupPoint(),
                match.getDropPoint(),
                match.getLeaveTime().toString(),
                match.getArriveTime().toString(),
                match.getPickupTime().toString(),
                match.getDropTime().toString()
        );
    }
}
