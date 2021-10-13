package com.commuting.commutingapp.trip.service.api;

import com.commuting.commutingapp.trip.dto.trip.active.ActiveDriverTripResponse;
import com.commuting.commutingapp.trip.dto.trip.active.ActivePassengerTripResponse;
import com.commuting.commutingapp.trip.dto.trip.ArrivedAtPickupRequest;
import com.commuting.commutingapp.trip.dto.trip.CompleteTripRequest;
import com.commuting.commutingapp.trip.dto.trip.DropPassengerRequest;
import com.commuting.commutingapp.trip.dto.trip.PickupPassengerRequest;
import com.commuting.commutingapp.trip.dto.trip.generic.GenericActiveTripResponse;

import java.util.List;

public interface ActiveTripService {

    void pickup(PickupPassengerRequest pickupPassengerRequest);

    void arrivedAtPickup(ArrivedAtPickupRequest arrivedAtPickupRequest);

    void drop(DropPassengerRequest dropPassengerRequest);

    List<GenericActiveTripResponse> getActive();

    ActivePassengerTripResponse getActiveForPassenger(String id);

    ActiveDriverTripResponse getActiveForDriver(String id);

    void complete(CompleteTripRequest completeTripRequest);
}
