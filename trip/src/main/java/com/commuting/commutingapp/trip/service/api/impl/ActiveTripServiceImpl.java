package com.commuting.commutingapp.trip.service.api.impl;

import com.commuting.commutingapp.common.utils.DateTimeUtils;
import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.location.mapper.LocationMapper;
import com.commuting.commutingapp.notification.model.Notification;
import com.commuting.commutingapp.notification.model.NotificationType;
import com.commuting.commutingapp.notification.service.NotificationService;
import com.commuting.commutingapp.profile.dto.response.CarResponse;
import com.commuting.commutingapp.profile.model.Profile;
import com.commuting.commutingapp.profile.service.CarService;
import com.commuting.commutingapp.profile.service.ProfileService;
import com.commuting.commutingapp.trip.dto.trip.active.ActiveDriverTripResponse;
import com.commuting.commutingapp.trip.dto.trip.active.ActivePassengerTripResponse;
import com.commuting.commutingapp.trip.dto.trip.common.PassengerResponse;
import com.commuting.commutingapp.trip.dto.trip.common.StopResponse;
import com.commuting.commutingapp.trip.dto.trip.ArrivedAtPickupRequest;
import com.commuting.commutingapp.trip.dto.trip.CompleteTripRequest;
import com.commuting.commutingapp.trip.dto.trip.DropPassengerRequest;
import com.commuting.commutingapp.trip.dto.trip.PickupPassengerRequest;
import com.commuting.commutingapp.trip.dto.trip.generic.GenericActiveTripResponse;
import com.commuting.commutingapp.trip.model.*;
import com.commuting.commutingapp.trip.repo.PassengerRepo;
import com.commuting.commutingapp.trip.repo.StopRepo;
import com.commuting.commutingapp.trip.repo.TripRepo;
import com.commuting.commutingapp.trip.service.api.ActiveTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ActiveTripServiceImpl implements ActiveTripService {

    @Autowired
    TripRepo tripRepo;

    @Autowired
    ProfileService profileService;

    @Autowired
    CarService carService;

    @Autowired
    StopRepo stopRepo;

    @Autowired
    PassengerRepo passengerRepo;

    @Autowired
    NotificationService notificationService;

    @Override
    @Transactional
    public void pickup(PickupPassengerRequest pickupPassengerRequest) {
        Trip trip = getTrip(pickupPassengerRequest.getTripId());
        Stop stop = getStop(pickupPassengerRequest.getStopId());
        long diff = ChronoUnit.MINUTES.between(stop.getTime(), DateTimeUtils.getTimeNow());
        stop.setTime(DateTimeUtils.getTimeNow());
        stop.setConfirmed(true);
        stopRepo.save(stop);
        applyTimeDeltaForStops(trip, stop, diff);
        trip.setArriveAt(trip.getArriveAt().plusMinutes(diff));
        notificationService.sendNotification(new Notification(stop.getUserId(), NotificationType.DRIVER_CONFIRMED_PICKUP, trip.getId()));
    }

    private void applyTimeDeltaForStops(Trip trip, Stop stop, long diff) {
        trip.getStops().stream().filter(s -> !s.isConfirmed() && !s.getId().equals(stop.getId())).forEach(s -> {
            s.setTime(s.getTime().plusMinutes(diff));
            stopRepo.save(s);
        });
        trip.getPassengers().forEach(p -> {
            p.setArriveTime(p.getArriveTime().plusMinutes(diff));
            passengerRepo.save(p);
        });
    }

    private Stop getStop(String stopId) {
        return stopRepo.findById(stopId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stop " + stopId + " not found!"));
    }

    private Passenger getPassenger(Trip trip, String passengerId) {
        return trip.getPassengers().stream().filter(p -> p.getUserId().equals(passengerId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger not found!"));
    }

    @Override
    @Transactional
    public void arrivedAtPickup(ArrivedAtPickupRequest arrivedAtPickupRequest) {
        Trip trip = getTrip(arrivedAtPickupRequest.getTripId());
        Stop stop = getStop(arrivedAtPickupRequest.getStopId());
        long diff = ChronoUnit.MINUTES.between(stop.getTime(), DateTimeUtils.getTimeNow());
        stop.setTime(DateTimeUtils.getTimeNow());
        stop.setArrived(true);
        stopRepo.save(stop);
        applyTimeDeltaForStops(trip, stop, diff);
        trip.setArriveAt(trip.getArriveAt().plusMinutes(diff));
        notificationService.sendNotification(new Notification(stop.getUserId(), NotificationType.DRIVER_ARRIVED_AT_PICKUP, trip.getId()));
    }

    @Override
    @Transactional
    public void drop(DropPassengerRequest dropPassengerRequest) {
        Trip trip = getTrip(dropPassengerRequest.getTripId());
        Stop stop = getStop(dropPassengerRequest.getStopId());
        Passenger passenger = getPassenger(trip, stop.getUserId());
        Profile driverProfile = profileService.getById(trip.getDriverId());
        long diff = ChronoUnit.MINUTES.between(stop.getTime(), DateTimeUtils.getTimeNow());
        stop.setTime(DateTimeUtils.getTimeNow());
        stop.setConfirmed(true);
        stopRepo.save(stop);
        applyTimeDeltaForStops(trip, stop, diff);
        trip.setArriveAt(trip.getArriveAt().plusMinutes(diff));
        DecimalFormat df = new DecimalFormat("#.##");
        driverProfile.addPoints(Double.parseDouble(df.format(passenger.getTripDistance() / 2000)));
        notificationService.sendNotification(new Notification(stop.getUserId(), NotificationType.DRIVER_CONFIRMED_DROP, trip.getId()));
    }

    @Override
    @Transactional
    public List<GenericActiveTripResponse> getActive() {
        String userId = SecurityUtils.getUserId();
        Stream<GenericActiveTripResponse> passengerTrips = tripRepo.findAllByStateAndPassengersUserIdAndPassengersCompleted(TripState.ACTIVE, userId, false)
                .stream()
                .map(trip -> getGenericActiveTripResponseForPassenger(userId, trip));
        Stream<GenericActiveTripResponse> driverTrips = tripRepo.findAllByStateAndDriverId(TripState.ACTIVE, userId)
                .stream()
                .map(this::getGenericActiveTripResponseForDriver);
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

    private GenericActiveTripResponse getGenericActiveTripResponseForDriver(Trip trip) {
        Optional<Stop> nextStopOpt = getNextStop(trip);
        return new GenericActiveTripResponse(
                trip.getId(),
                LocationMapper.buildLocationResponse(trip.getFrom()),
                LocationMapper.buildLocationResponse(trip.getTo()),
                trip.getArriveAt().toString(),
                true,
                nextStopOpt.map(nextStop -> nextStop.getTime().toString()).orElse(null),
                false,
                nextStopOpt.map(nextStop -> profileService.getById(nextStop.getUserId()).getFirstName()).orElse(null),
                nextStopOpt.map(Stop::isPickup).orElse(false)
        );
    }

    private GenericActiveTripResponse getGenericActiveTripResponseForPassenger(String userId, Trip trip) {
        Optional<Stop> nextStopOpt = getNextStop(trip);
        Passenger passenger = getPassenger(trip, userId);
        return new GenericActiveTripResponse(
                trip.getId(),
                LocationMapper.buildLocationResponse(passenger.getFrom()),
                LocationMapper.buildLocationResponse(passenger.getTo()),
                passenger.getArriveTime().toString(),
                false,
                nextStopOpt.map(nextStop -> nextStop.getTime().toString()).orElse(null),
                nextStopOpt.filter(nextStop -> nextStop.getUserId().equals(userId)).isPresent(),
                null,
                nextStopOpt.map(Stop::isPickup).orElse(false)
        );
    }

    @Override
    @Transactional
    public ActivePassengerTripResponse getActiveForPassenger(String id) {
        String userId = SecurityUtils.getUserId();
        Trip trip = tripRepo.findById(id).orElseThrow();

        verifyTripActiveStatus(trip);

        Profile profile = profileService.getById(trip.getDriverId());
        CarResponse car = carService.getCar(trip.getCarId());
        Passenger passenger = getPassenger(trip, userId);

        Stop pickup = trip.getStops().stream().filter(stop -> stop.getUserId().equals(userId)).filter(Stop::isPickup).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pickup not found!"));
        Stop drop = trip.getStops().stream().filter(stop -> stop.getUserId().equals(userId)).filter(stop -> !stop.isPickup()).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Drop not found!"));

        return new ActivePassengerTripResponse(
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
                trip.getPath()
        );
    }

    private void verifyTripActiveStatus(Trip trip) {
        if (trip.getStatus() != TripStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trip is not active");
        }
    }

    @Override
    @Transactional
    public ActiveDriverTripResponse getActiveForDriver(String id) {
        Trip trip = tripRepo.findById(id).orElseThrow();

        verifyTripActiveStatus(trip);

        Set<Stop> stops = trip.getStops();
        Set<Passenger> passengers = trip.getPassengers();
        CarResponse car = carService.getCar(trip.getCarId());


        return new ActiveDriverTripResponse(
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
                        )).collect(Collectors.toList())
        );
    }

    @Override
    @Transactional
    public void complete(CompleteTripRequest completeTripRequest) {
        Trip trip = getTrip(completeTripRequest.getTripId());
        trip.setStatus(TripStatus.COMPLETED);
        trip.setState(TripState.PAST);
        tripRepo.save(trip);
        trip.getPassengers().forEach(passenger -> {
            notificationService.sendNotification(new Notification(passenger.getUserId(), NotificationType.TRIP_COMPLETED, trip.getId()));
        });
    }

    private Trip getTrip(String tripId) {
        return tripRepo.findById(tripId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip " + tripId + " not found!"));
    }

    private Optional<Stop> getNextStop(Trip trip) {
        return trip.getStops().stream().filter(stop -> !stop.isConfirmed()).min(Comparator.comparing(Stop::getTime));
    }

}
