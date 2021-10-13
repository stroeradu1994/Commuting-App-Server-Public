package com.commuting.commutingapp.trip.service.internal.impl;

import com.commuting.commutingapp.common.dto.Point;
import com.commuting.commutingapp.common.utils.LocationUtils;
import com.commuting.commutingapp.trip.model.Bound;
import com.commuting.commutingapp.trip.model.Trip;
import com.commuting.commutingapp.trip.model.TripRequest;
import com.commuting.commutingapp.trip.repo.BoundRepo;
import com.commuting.commutingapp.trip.service.internal.BoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BoundServiceImpl implements BoundService {

    @Autowired
    BoundRepo boundRepo;

    public Bound saveBound(Trip trip, String ne, String sw) {
        Bound bound = new Bound(LocationUtils.locationFromString(ne).getLat(), LocationUtils.locationFromString(ne).getLng(), LocationUtils.locationFromString(sw).getLat(), LocationUtils.locationFromString(sw).getLng());
        increaseBoundsByMeters(bound, 1000);
        bound.setTrip(trip);
        return boundRepo.save(bound);
    }

    public Bound saveBound(TripRequest tripRequest, Set<Point> points) {
        Bound bound = calculateBound(points);
        bound.setTripRequest(tripRequest);
        return boundRepo.save(bound);
    }

    @Override
    public Bound calculateBound(Set<Point> points) {

        Bound bound = new Bound();
        Set<Double> latitudes = points.stream().map(Point::getLat).collect(Collectors.toSet());
        Set<Double> longitudes = points.stream().map(Point::getLng).collect(Collectors.toSet());

        bound.setNeLat(latitudes.stream().max(Double::compareTo).get());
        bound.setNeLng(longitudes.stream().max(Double::compareTo).get());
        bound.setSwLat(latitudes.stream().min(Double::compareTo).get());
        bound.setSwLng(longitudes.stream().min(Double::compareTo).get());

        return bound;
    }


    public void increaseBoundsByMeters(Bound bound, int meters) {

        Point newNePoint = LocationUtils.moveLatLngToRightUp(new Point(bound.getNeLat(), bound.getNeLng()), meters);
        Point newSwPoint = LocationUtils.moveLatLngToLeftDown(new Point(bound.getSwLat(), bound.getSwLng()), meters);

        bound.setNeLat(newNePoint.getLat());
        bound.setNeLng(newNePoint.getLng());
        bound.setSwLat(newSwPoint.getLat());
        bound.setSwLng(newSwPoint.getLng());
    }
}
