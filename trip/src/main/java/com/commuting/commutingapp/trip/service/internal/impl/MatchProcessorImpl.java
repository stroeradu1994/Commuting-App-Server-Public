package com.commuting.commutingapp.trip.service.internal.impl;

import com.commuting.commutingapp.common.dto.Point;
import com.commuting.commutingapp.trip.dto.internal.PointWithDistanceAndTimeDeltaPair;
import com.commuting.commutingapp.common.dto.PointWithTimeDeltaAndDistanceDelta;
import com.commuting.commutingapp.trip.dto.internal.PointWithWalkingDistanceTimeDeltaDistanceDelta;
import com.commuting.commutingapp.common.utils.LocationUtils;
import com.commuting.commutingapp.trip.model.Bound;
import com.commuting.commutingapp.trip.model.*;
import com.commuting.commutingapp.trip.repo.MatchRepo;
import com.commuting.commutingapp.trip.repo.TripRepo;
import com.commuting.commutingapp.trip.repo.TripRequestRepo;
import com.commuting.commutingapp.trip.service.cache.PointsCache;
import com.commuting.commutingapp.trip.service.internal.MatchProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MatchProcessorImpl implements MatchProcessor {

    @Autowired
    TripRepo tripRepo;

    @Autowired
    TripRequestRepo tripRequestRepo;

    @Autowired
    PointsCache pointsCache;

    @Autowired
    MatchRepo matchRepo;

    @Override
    @Transactional
    public void generateMatchesForTripRequest(TripRequest tripRequest) {
        Bound bound = tripRequest.getBound();
        LocalDateTime arriveBy = tripRequest.getArriveBy();
        tripRepo.findAllByStateAndBoundNeLatGreaterThanAndBoundNeLngGreaterThanAndBoundSwLatLessThanAndBoundSwLngLessThanAndArriveAtLessThanAndArriveAtGreaterThan(
                TripState.UPCOMING, bound.getNeLat(), bound.getNeLng(), bound.getSwLat(), bound.getSwLng(), arriveBy, arriveBy.minusHours(3)
        )
                .stream()
                .flatMap(trip -> createMatchIfSuitable(tripRequest, trip))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public Set<Match> generateMatchesForTrip(Trip trip) {
        Bound bound = trip.getBound();
        LocalDateTime arriveAt = trip.getArriveAt();
        return tripRequestRepo.findAllByConfirmedAndBoundNeLatLessThanAndBoundNeLngLessThanAndBoundSwLatGreaterThanAndBoundSwLngGreaterThanAndArriveByGreaterThanAndArriveByLessThan(
                false, bound.getNeLat(), bound.getNeLng(), bound.getSwLat(), bound.getSwLng(), arriveAt, arriveAt.plusHours(3)
        )
                .stream()
                .flatMap(tripRequest -> createMatchIfSuitable(tripRequest, trip))
                .collect(Collectors.toSet());
    }

    private Stream<Match> createMatchIfSuitable(TripRequest tripRequest, Trip trip) {
        return getPickupAndDropPoints(tripRequest, trip)
                .stream()
                .map(pointWithDistanceAndTimeDeltaPair -> saveMatch(tripRequest, trip, pointWithDistanceAndTimeDeltaPair));
    }

    private Match saveMatch(TripRequest tripRequest, Trip trip, PointWithDistanceAndTimeDeltaPair pointWithDistanceAndTimeDeltaPair) {
        Point pickupPoint = pointWithDistanceAndTimeDeltaPair.getPickup().getPoint();
        Point dropPoint = pointWithDistanceAndTimeDeltaPair.getDrop().getPoint();

        double pickupWalkingDistance = pointWithDistanceAndTimeDeltaPair.getPickup().getDistance();
        double dropWalkingDistance = pointWithDistanceAndTimeDeltaPair.getDrop().getDistance();

        LocalDateTime pickupTime = trip.getLeaveAt().plusSeconds((long) pointWithDistanceAndTimeDeltaPair.getPickup().getTimeDelta());
        LocalDateTime leaveTime = pickupTime.minusSeconds((long) pickupWalkingDistance);

        LocalDateTime dropTime = trip.getLeaveAt().plusSeconds((long) pointWithDistanceAndTimeDeltaPair.getDrop().getTimeDelta());
        LocalDateTime arriveTime = dropTime.plusSeconds((long) dropWalkingDistance);

        double pickupDistanceDelta = pointWithDistanceAndTimeDeltaPair.getPickup().getDistanceDelta();
        double dropDistanceDelta = pointWithDistanceAndTimeDeltaPair.getDrop().getDistanceDelta();

        Match match = new Match(trip, tripRequest, tripRequest.getUserId(), dropDistanceDelta - pickupDistanceDelta, pickupWalkingDistance, dropWalkingDistance, pickupPoint.toString(), dropPoint.toString(), leaveTime, arriveTime, pickupTime, dropTime);
        return matchRepo.save(match);
    }

    private Optional<PointWithDistanceAndTimeDeltaPair> getPickupAndDropPoints(TripRequest tripRequest, Trip trip) {
        LinkedHashSet<PointWithTimeDeltaAndDistanceDelta> points = pointsCache.getFromCache(trip.getId(), trip.getRoute());

        Point passengerFromPoint = new Point(tripRequest.getFrom().getPoint().getLat(), tripRequest.getFrom().getPoint().getLng());
        Point passengerToPoint = new Point(tripRequest.getTo().getPoint().getLat(), tripRequest.getTo().getPoint().getLng());

        PointWithWalkingDistanceTimeDeltaDistanceDelta minimumPassengerFromPoint = getPointWithDistanceAndTimeDelta(points, passengerFromPoint);
        PointWithWalkingDistanceTimeDeltaDistanceDelta minimumPassengerToPoint = getPointWithDistanceAndTimeDelta(points, passengerToPoint);

        return (minimumPassengerFromPoint.getDistance() < 1000 &&
                minimumPassengerToPoint.getDistance() < 1000 &&
                isFromBeforeTo(minimumPassengerFromPoint, minimumPassengerToPoint)) ?
                    Optional.of(new PointWithDistanceAndTimeDeltaPair(minimumPassengerFromPoint, minimumPassengerToPoint)) :
                    Optional.empty();
    }

    private PointWithWalkingDistanceTimeDeltaDistanceDelta getPointWithDistanceAndTimeDelta(Set<PointWithTimeDeltaAndDistanceDelta> points, Point fromPoint) {
        return points.stream()
                .map(point -> new PointWithWalkingDistanceTimeDeltaDistanceDelta(
                        point.getPoint(),
                        LocationUtils.distance(point.getPoint(), fromPoint),
                        point.getTimeDelta(),
                        point.getDistanceDelta()
                )).reduce((x, y) -> x.getDistance() < y.getDistance() ? x : y)
                .get();
    }

    private boolean isFromBeforeTo(PointWithWalkingDistanceTimeDeltaDistanceDelta minimumFromPoint, PointWithWalkingDistanceTimeDeltaDistanceDelta minimumToPoint) {
        return minimumFromPoint.getDistanceDelta() < minimumToPoint.getDistanceDelta();
    }
}
