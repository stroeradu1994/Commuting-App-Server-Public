package com.commuting.commutingapp.trip.service.api;

import com.commuting.commutingapp.trip.dto.trip.TripAvailabilityRequest;

public interface TripAvailabilityService {

    void checkAvailability(TripAvailabilityRequest tripAvailabilityRequest);
}
