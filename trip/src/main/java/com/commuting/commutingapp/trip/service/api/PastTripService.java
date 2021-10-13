package com.commuting.commutingapp.trip.service.api;

import com.commuting.commutingapp.trip.dto.trip.generic.GenericPastTripResponse;
import com.commuting.commutingapp.trip.dto.trip.past.PastDriverTripResponse;
import com.commuting.commutingapp.trip.dto.trip.past.PastPassengerTripResponse;

import java.util.List;

public interface PastTripService {

    List<GenericPastTripResponse> getPast();

    PastPassengerTripResponse getPastForPassenger(String id);

    PastDriverTripResponse getPastForDriver(String id);

}
