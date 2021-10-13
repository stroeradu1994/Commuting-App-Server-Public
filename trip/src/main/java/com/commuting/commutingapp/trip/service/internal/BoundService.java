package com.commuting.commutingapp.trip.service.internal;

import com.commuting.commutingapp.common.dto.Point;
import com.commuting.commutingapp.trip.model.Bound;
import com.commuting.commutingapp.trip.model.Trip;
import com.commuting.commutingapp.trip.model.TripRequest;

import java.util.Set;

public interface BoundService {

    Bound saveBound(Trip trip, String ne, String sw);

    Bound saveBound(TripRequest tripRequest, Set<Point> points);

    Bound calculateBound(Set<Point> points);

    void increaseBoundsByMeters(Bound bound, int meters);

}
