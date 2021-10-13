package com.commuting.commutingapp.trip.service.api.impl;

import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.location.mapper.LocationMapper;
import com.commuting.commutingapp.profile.dto.response.CarResponse;
import com.commuting.commutingapp.profile.model.Profile;
import com.commuting.commutingapp.profile.service.CarService;
import com.commuting.commutingapp.profile.service.ProfileService;
import com.commuting.commutingapp.trip.dto.trip.common.PassengerResponse;
import com.commuting.commutingapp.trip.dto.trip.past.PastDriverTripResponse;
import com.commuting.commutingapp.trip.dto.trip.past.PastPassengerTripResponse;
import com.commuting.commutingapp.trip.dto.trip.common.StopResponse;
import com.commuting.commutingapp.trip.dto.trip.generic.GenericPastTripResponse;
import com.commuting.commutingapp.trip.model.Passenger;
import com.commuting.commutingapp.trip.model.Stop;
import com.commuting.commutingapp.trip.model.Trip;
import com.commuting.commutingapp.trip.model.TripState;
import com.commuting.commutingapp.trip.repo.TripRepo;
import com.commuting.commutingapp.trip.service.api.PastTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PastTripServiceImpl implements PastTripService {

    @Autowired
    TripRepo tripRepo;

    @Autowired
    ProfileService profileService;

    @Autowired
    CarService carService;

    @Override
    @Transactional
    public List<GenericPastTripResponse> getPast() {
        String userId = SecurityUtils.getUserId();
        Stream<GenericPastTripResponse> passengerTrips = tripRepo.findAllByStateAndPassengersUserIdAndPassengersCompleted(TripState.PAST, userId, false)
                .stream()
                .map(trip -> {
                    return getGenericPastTripResponseForPassenger(userId, trip);
                });
        Stream<GenericPastTripResponse> driverTrips = tripRepo.findAllByStateAndDriverId(TripState.PAST, userId)
                .stream()
                .map(trip -> new GenericPastTripResponse(
                        trip.getId(),
                        LocationMapper.buildLocationResponse(trip.getFrom()),
                        LocationMapper.buildLocationResponse(trip.getTo()),
                        trip.getLeaveAt().toString(),
                        trip.getArriveAt().toString(),
                        true,
                        trip.getStatus()
                ));
        return Stream.concat(passengerTrips, driverTrips)
                .sorted((a, b) -> {
                    LocalDateTime aTime = LocalDateTime.parse(a.getArriveTime());
                    LocalDateTime bTime = LocalDateTime.parse(b.getArriveTime());
                    if (aTime.isBefore(bTime)) {
                        return 1;
                    }
                    if (aTime.isAfter(bTime)) {
                        return -1;
                    }
                    return 0;
                }).collect(Collectors.toList());
    }

    private GenericPastTripResponse getGenericPastTripResponseForPassenger(String userId, Trip trip) {
        Passenger passenger = trip.getPassengers().stream().filter(pas -> pas.getUserId().equals(userId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger not found!"));
        return new GenericPastTripResponse(
                trip.getId(),
                LocationMapper.buildLocationResponse(passenger.getFrom()),
                LocationMapper.buildLocationResponse(passenger.getTo()),
                passenger.getLeaveTime().toString(),
                passenger.getArriveTime().toString(),
                false,
                trip.getStatus()
        );
    }

    @Override
    @Transactional
    public PastPassengerTripResponse getPastForPassenger(String id) {
        String userId = SecurityUtils.getUserId();
        Trip trip = tripRepo.findById(id).orElseThrow();
        Profile profile = profileService.getById(trip.getDriverId());
        CarResponse car = carService.getCar(trip.getCarId());
        Passenger passenger = trip.getPassengers().stream().filter(pas -> pas.getUserId().equals(userId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger not found!"));

        if (trip.getState() != TripState.PAST) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trip is not completed");
        }

        Stop pickup = trip.getStops().stream().filter(stop -> stop.getUserId().equals(userId)).filter(Stop::isPickup).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pickup not found!"));
        Stop drop = trip.getStops().stream().filter(stop -> stop.getUserId().equals(userId)).filter(stop -> !stop.isPickup()).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Drop not found!"));

        return new PastPassengerTripResponse(
                trip.getId(),
                trip.getDriverId(),
                profile.getFirstName() + " " + profile.getLastName(),
                profile.getRating(),
                car.getBrand(),
                car.getModel(),
                car.getColor(),
                car.getPlate(),
                LocationMapper.buildLocationResponse(passenger.getFrom()),
                LocationMapper.buildLocationResponse(passenger.getTo()),
                pickup.isConfirmed(),
                drop.isConfirmed(),
                pickup.isArrived(),
                pickup.getWalkingDistance(),
                drop.getWalkingDistance(),
                passenger.getTripDistance(),
                pickup.getPoint(),
                pickup.getAddress(),
                drop.getPoint(),
                drop.getAddress(),
                passenger.getLeaveTime().toString(),
                passenger.getArriveTime().toString(),
                pickup.getTime().toString(),
                drop.getTime().toString(),
                trip.getPath(),
                trip.getStatus(),
                passenger.getRating()
        );
    }

    @Override
    @Transactional
    public PastDriverTripResponse getPastForDriver(String id) {
        Trip trip = tripRepo.findById(id).orElseThrow();
        Set<Stop> stops = trip.getStops();
        Set<Passenger> passengers = trip.getPassengers();
        CarResponse car = carService.getCar(trip.getCarId());

        return new PastDriverTripResponse(
                trip.getId(),
                car.getBrand(),
                car.getModel(),
                car.getColor(),
                car.getPlate(),
                LocationMapper.buildLocationResponse(trip.getFrom()),
                LocationMapper.buildLocationResponse(trip.getTo()),
                trip.getLeaveAt().toString(),
                trip.getArriveAt().toString(),
                trip.getPath(),
                trip.getStatus(),
                passengers.stream().map(passenger -> {
                    Profile profile = profileService.getById(passenger.getUserId());
                    return new PassengerResponse(
                            passenger.getUserId(),
                            profile.getFirstName(),
                            profile.getLastName(),
                            profile.getRating()
                    );
                }).collect(Collectors.toList()),
                stops.stream()
                        .sorted(Comparator.comparing(Stop::getTime))
                        .map(stop -> new StopResponse(
                                stop.getId(),
                                stop.getPoint(),
                                stop.getAddress(),
                                stop.isPickup(),
                                stop.isArrived(),
                                stop.getTime().toString(),
                                stop.getUserId(),
                                profileService.getById(stop.getUserId()).getFirstName(),
                                stop.isConfirmed()
                        )).collect(Collectors.toList()),
                getDriverRating(passengers)

        );
    }

    private float getDriverRating(Set<Passenger> passengers) {
        Set<Integer> ratings = passengers.stream().map(Passenger::getRating).filter(rating -> rating != -1).collect(Collectors.toSet());
        return ratings.size() != 0 ? ((float) ratings.stream().mapToInt(a -> a).sum() / ratings.size()) : 0;
    }
}
