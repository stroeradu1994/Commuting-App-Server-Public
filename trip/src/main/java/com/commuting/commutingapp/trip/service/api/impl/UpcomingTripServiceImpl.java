package com.commuting.commutingapp.trip.service.api.impl;

import com.commuting.commutingapp.common.utils.DateTimeUtils;
import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.direction.model.Route;
import com.commuting.commutingapp.direction.service.api.DirectionService;
import com.commuting.commutingapp.event.model.Event;
import com.commuting.commutingapp.event.model.EventType;
import com.commuting.commutingapp.event.service.EventService;
import com.commuting.commutingapp.location.dto.location.internal.LocationPair;
import com.commuting.commutingapp.location.mapper.LocationMapper;
import com.commuting.commutingapp.location.service.api.LocationService;
import com.commuting.commutingapp.notification.model.Notification;
import com.commuting.commutingapp.notification.model.NotificationType;
import com.commuting.commutingapp.notification.service.NotificationService;
import com.commuting.commutingapp.profile.dto.response.CarResponse;
import com.commuting.commutingapp.profile.model.Profile;
import com.commuting.commutingapp.profile.service.CarService;
import com.commuting.commutingapp.profile.service.ProfileService;
import com.commuting.commutingapp.trip.dto.trip.ConfirmTripRequest;
import com.commuting.commutingapp.trip.dto.trip.CreateTripDto;
import com.commuting.commutingapp.trip.dto.trip.generic.GenericUpcomingTripResponse;
import com.commuting.commutingapp.trip.dto.trip.upcoming.UpcomingDriverTripResponse;
import com.commuting.commutingapp.trip.dto.trip.upcoming.UpcomingPassengerResponse;
import com.commuting.commutingapp.trip.dto.trip.upcoming.UpcomingPassengerTripResponse;
import com.commuting.commutingapp.trip.model.*;
import com.commuting.commutingapp.trip.repo.*;
import com.commuting.commutingapp.trip.service.api.UpcomingTripService;
import com.commuting.commutingapp.trip.service.cache.PointsCache;
import com.commuting.commutingapp.trip.service.internal.BoundService;
import com.commuting.commutingapp.trip.service.internal.MatchProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UpcomingTripServiceImpl implements UpcomingTripService {

    @Autowired
    TripRepo tripRepo;

    @Autowired
    LocationService locationService;

    @Autowired
    MatchProcessor matchProcessor;

    @Autowired
    MatchRepo matchRepo;

    @Autowired
    PointsCache pointsCache;

    @Autowired
    BoundService boundService;

    @Autowired
    DirectionService directionService;

    @Autowired
    ProfileService profileService;

    @Autowired
    CarService carService;

    @Autowired
    TripRequestRepo tripRequestRepo;

    @Autowired
    PassengerRepo passengerRepo;

    @Autowired
    StopRepo stopRepo;

    @Autowired
    NotificationService notificationService;

    @Autowired
    EventService eventService;

    @Override
    @Transactional
    public String create(CreateTripDto createTripDto) {
        Route route = directionService.saveRoute(createTripDto.getRouteId());
        LocationPair locationPair = locationService.getLocationPair(createTripDto.getFrom(), createTripDto.getTo());
        Trip trip = buildTripForCreate(createTripDto, locationPair, route);
        Trip savedTrip = tripRepo.save(trip);

        Bound savedBound = boundService.saveBound(trip, route.getNe(), route.getSw());

        savedTrip.setBound(savedBound);
        savedTrip = tripRepo.save(savedTrip);

        pointsCache.getFromCache(savedTrip.getId(), route);

        Set<Match> matches = matchProcessor.generateMatchesForTrip(savedTrip);
        matches.stream().filter(match -> !match.isConfirmed()).forEach(match -> notificationService.sendNotification(new Notification(match.getTripRequest().getUserId(), NotificationType.MATCH_FOUND, match.getTripRequest().getId())));

        eventService.logEvent(new Event(SecurityUtils.getUserId(), EventType.CREATE_TRIP));
        return savedTrip.getId();
    }

    @Override
    @Transactional
    public void confirm(ConfirmTripRequest confirmTripRequest) {
        Trip trip = getTrip(confirmTripRequest.getTripId());
        trip.setStatus(TripStatus.CONFIRMED); // confirmed
        long diff = ChronoUnit.MINUTES.between(trip.getLeaveAt(), LocalDateTime.parse(confirmTripRequest.getLeaveAt()));
        trip.setLeaveAt(LocalDateTime.parse(confirmTripRequest.getLeaveAt()));
        trip.setArriveAt(trip.getArriveAt().plusMinutes(diff));
        tripRepo.save(trip);
        updateMatchWithTimeDifference(trip, diff, NotificationType.TRIP_CONFIRMED);
        updatePassengersWithTimeDifference(trip, diff);
        updateStopsWithTimeDifference(trip, diff);
    }

    private void updatePassengersWithTimeDifference(Trip trip, long diff) {
        trip.getPassengers().forEach(p -> {
            p.setArriveTime(p.getArriveTime().plusMinutes(diff));
            p.setLeaveTime(p.getLeaveTime().plusMinutes(diff));
            passengerRepo.save(p);
        });
    }

    private void updateStopsWithTimeDifference(Trip trip, long diff) {
        trip.getStops().forEach(stop -> {
            stop.setTime(stop.getTime().plusMinutes(diff));
            stopRepo.save(stop);
        });
    }

    private void updateMatchWithTimeDifference(Trip trip, long diff, NotificationType tripConfirmed) {
        trip.getMatches().stream()
                .filter(Match::isConfirmed)
                .peek(match -> updateMatch(diff, match))
                .map(Match::getTripRequest)
                .map(TripRequest::getUserId)
                .forEach(passenger -> notificationService.sendNotification(new Notification(passenger, tripConfirmed, trip.getId())));
    }

    private void updateMatch(long diff, Match match) {
        match.setArriveTime(match.getArriveTime().plusMinutes(diff));
        match.setLeaveTime(match.getLeaveTime().plusMinutes(diff));
        match.setPickupTime(match.getPickupTime().plusMinutes(diff));
        match.setDropTime(match.getDropTime().plusMinutes(diff));
        matchRepo.save(match);
    }

    @Override
    @Transactional
    public void start(String id) {
        Trip trip = getTrip(id);
        trip.setStatus(TripStatus.ACTIVE);
        trip.setState(TripState.ACTIVE);
        long diff = ChronoUnit.MINUTES.between(trip.getLeaveAt(), DateTimeUtils.getTimeNow());
        trip.setLeaveAt(DateTimeUtils.getTimeNow());
        trip.setArriveAt(trip.getArriveAt().plusMinutes(diff));
        tripRepo.save(trip);

        updateMatchWithTimeDifference(trip, diff, NotificationType.TRIP_STARTED);
        updatePassengersWithTimeDifference(trip, diff);
        updateStopsWithTimeDifference(trip, diff);
    }

    @Override
    @Transactional
    public List<GenericUpcomingTripResponse> getUpcoming() {
        String userId = SecurityUtils.getUserId();
        Stream<GenericUpcomingTripResponse> passengerTrips = tripRepo.findAllByStateAndPassengersUserId(TripState.UPCOMING, userId)
                .stream()
                .map(trip -> {
                    Passenger passenger = trip.getPassengers().stream().filter(p -> p.getUserId().equals(userId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger not found!"));
                    return new GenericUpcomingTripResponse(
                            trip.getId(),
                            LocationMapper.buildLocationResponse(passenger.getFrom()),
                            LocationMapper.buildLocationResponse(passenger.getTo()),
                            passenger.getLeaveTime().toString(),
                            passenger.getArriveTime().toString(),
                            (int) trip.getMatches().stream().filter(Match::isConfirmed).count(),
                            false,
                            trip.getStatus() == TripStatus.CONFIRMED
                    );
                });
        Stream<GenericUpcomingTripResponse> driverTrips =
                tripRepo.findAllByStateAndDriverId(TripState.UPCOMING, userId)
                        .stream()
                        .map(trip -> new GenericUpcomingTripResponse(
                                trip.getId(),
                                LocationMapper.buildLocationResponse(trip.getFrom()),
                                LocationMapper.buildLocationResponse(trip.getTo()),
                                trip.getLeaveAt().toString(),
                                trip.getArriveAt().toString(),
                                (int) trip.getMatches().stream().filter(Match::isConfirmed).count(),
                                true,
                                trip.getStatus() == TripStatus.CONFIRMED
                        ));
        return Stream.concat(passengerTrips, driverTrips)
                .sorted((a, b) -> {
                    LocalDateTime aTime = LocalDateTime.parse(a.getLeaveTime());
                    LocalDateTime bTime = LocalDateTime.parse(b.getLeaveTime());
                    if (aTime.isBefore(bTime)) {
                        return 1;
                    }
                    if (aTime.isAfter(bTime)) {
                        return -1;
                    }
                    return 0;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UpcomingPassengerTripResponse getUpcomingForPassenger(String id) {
        String userId = SecurityUtils.getUserId();
        Trip trip = getTrip(id);
        String driverId = trip.getDriverId();
        String carId = trip.getCarId();

        Match match = trip.getMatches().stream().filter(m -> m.getTripRequest().getUserId().equals(userId)).findFirst().orElseThrow();

        Profile profile = profileService.getById(driverId);
        CarResponse carResponse = carService.getCar(carId);

        return new UpcomingPassengerTripResponse(
                trip.getId(),
                trip.getDriverId(),
                profile.getFirstName() + ' ' + profile.getLastName(),
                profile.getRating(),
                carResponse.getBrand(),
                carResponse.getModel(),
                carResponse.getColor(),
                LocationMapper.buildLocationResponse(match.getTripRequest().getFrom()),
                LocationMapper.buildLocationResponse(match.getTripRequest().getTo()),
                match.getPickupWalkingDistance(),
                match.getDropWalkingDistance(),
                match.getPickupPoint(),
                match.getDropPoint(),
                match.getLeaveTime().toString(),
                match.getArriveTime().toString(),
                match.getPickupTime().toString(),
                match.getDropTime().toString(),
                trip.getPath(),
                trip.getStatus() == TripStatus.CONFIRMED
        );
    }

    @Override
    @Transactional
    public UpcomingDriverTripResponse getUpcomingForDriver(String id) {
        Trip trip = getTrip(id);
        CarResponse carResponse = carService.getCar(trip.getCarId());

        Set<Match> confirmedMatches = trip.getMatches().stream().filter(Match::isConfirmed).collect(Collectors.toSet());

        return new UpcomingDriverTripResponse(
                trip.getId(),
                carResponse.getBrand(),
                carResponse.getModel(),
                carResponse.getColor(),
                carResponse.getPlate(),
                LocationMapper.buildLocationResponse(trip.getFrom()),
                LocationMapper.buildLocationResponse(trip.getTo()),
                trip.getLeaveAt().toString(),
                trip.getArriveAt().toString(),
                trip.getPath(),
                confirmedMatches.stream().map(match -> {
                    Profile profile = profileService.getById(match.getTripRequest().getUserId());
                    return new UpcomingPassengerResponse(
                            match.getUserId(),
                            profile.getFirstName(),
                            profile.getLastName(),
                            match.getPickupPoint(),
                            match.getDropPoint(),
                            match.getPickupTime().toString(),
                            match.getDropTime().toString(),
                            profile.getRating()
                    );
                }).collect(Collectors.toList()),
                trip.getStatus() == TripStatus.CONFIRMED
        );
    }

    @Override
    @Transactional
    public void cancel(String id) {
        Trip trip = getTrip(id);
        trip.setState(TripState.PAST);
        trip.setStatus(TripStatus.CANCELLED);
        trip.getMatches().stream().filter(Match::isConfirmed).forEach(match -> {
            match.setConfirmed(false);
            TripRequest tripRequest = match.getTripRequest();
            tripRequest.setConfirmed(false);

            matchRepo.save(match);
            tripRequestRepo.save(tripRequest);
        });
    }

    private Trip buildTripForCreate(CreateTripDto createTripDto, LocationPair locationPair, Route route) {
        Trip trip = new Trip();
        trip.setFrom(locationPair.getFrom());
        trip.setTo(locationPair.getTo());
        trip.setLeaveAt(LocalDateTime.parse(createTripDto.getLeaveAt()));
        trip.setArriveAt(LocalDateTime.parse(createTripDto.getLeaveAt()).plusSeconds((long) route.getDuration()));
        trip.setCreatedAt(DateTimeUtils.getTimeNow());
        trip.setStatus(TripStatus.IDLE);
        trip.setState(TripState.UPCOMING);
        trip.setPath(route.getPath());
        trip.setCarId(createTripDto.getCarId());
        trip.setRoute(route);
        trip.setDriverId(SecurityUtils.getUserId());

        return trip;
    }

    private Trip getTrip(String tripId) {
        return tripRepo.findById(tripId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip " + tripId + " not found!"));
    }
}
