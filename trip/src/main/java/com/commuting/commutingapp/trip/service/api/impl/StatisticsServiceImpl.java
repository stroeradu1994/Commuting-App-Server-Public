package com.commuting.commutingapp.trip.service.api.impl;

import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.trip.dto.statistic.StatisticsResponse;
import com.commuting.commutingapp.trip.model.Passenger;
import com.commuting.commutingapp.trip.model.Trip;
import com.commuting.commutingapp.trip.model.TripState;
import com.commuting.commutingapp.trip.repo.TripRepo;
import com.commuting.commutingapp.trip.repo.TripRequestRepo;
import com.commuting.commutingapp.trip.service.api.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    TripRepo tripRepo;

    @Autowired
    TripRequestRepo tripRequestRepo;

    @Override
    public StatisticsResponse get() {
        String userId = SecurityUtils.getUserId();

        double numberOfTripsAsDriver = 0;
        double numberOfTripsAsPassenger = 0;
        double kmAsDriver = 0;
        double kmAsPassenger = 0;
        double numberOfPassengers = 0;
        double kmPassengersRodeWithYou = 0;

        List<Trip> allDriverTrips = tripRepo.findAllByStateAndDriverId(TripState.PAST, userId);
        List<Trip> allPassengerTrips = tripRepo.findAllByStateAndPassengersUserId(TripState.PAST, userId);

        for (Trip trip : allDriverTrips) {
            numberOfTripsAsDriver++;
            kmAsDriver = kmAsDriver + trip.getRoute().getDistance();
            numberOfPassengers = numberOfPassengers + trip.getPassengers().size();
        }

        for (Trip trip : allPassengerTrips) {
            numberOfTripsAsPassenger++;
            Optional<Passenger> passengerOptional = trip.getPassengers().stream().filter(pas -> pas.getUserId().equals(userId)).findFirst();
            if (passengerOptional.isPresent()) {
                Passenger passenger = passengerOptional.get();
                kmAsPassenger = kmAsPassenger + passenger.getTripDistance();
            }
        }

        return new StatisticsResponse(numberOfTripsAsDriver, numberOfTripsAsPassenger, kmAsDriver / 1000, kmAsPassenger / 1000, numberOfPassengers);
    }
}
