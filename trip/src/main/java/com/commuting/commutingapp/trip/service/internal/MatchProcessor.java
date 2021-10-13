package com.commuting.commutingapp.trip.service.internal;

import com.commuting.commutingapp.trip.model.Bound;
import com.commuting.commutingapp.trip.model.Match;
import com.commuting.commutingapp.trip.model.Trip;
import com.commuting.commutingapp.trip.model.TripRequest;

import java.util.Set;

public interface MatchProcessor {
    void generateMatchesForTripRequest(TripRequest tripRequest);

    Set<Match> generateMatchesForTrip(Trip trip);

}
