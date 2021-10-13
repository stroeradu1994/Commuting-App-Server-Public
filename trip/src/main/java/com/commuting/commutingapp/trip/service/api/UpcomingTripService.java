package com.commuting.commutingapp.trip.service.api;

import com.commuting.commutingapp.trip.dto.trip.*;
import com.commuting.commutingapp.trip.dto.trip.generic.GenericUpcomingTripResponse;
import com.commuting.commutingapp.trip.dto.trip.upcoming.UpcomingDriverTripResponse;
import com.commuting.commutingapp.trip.dto.trip.upcoming.UpcomingPassengerTripResponse;

import java.util.List;

public interface UpcomingTripService {

    String create(CreateTripDto createTripDto);

    void confirm(ConfirmTripRequest confirmTripRequest);

    void start(String id);

    List<GenericUpcomingTripResponse> getUpcoming();

    UpcomingPassengerTripResponse getUpcomingForPassenger(String id);

    UpcomingDriverTripResponse getUpcomingForDriver(String id);

    void cancel(String id);
}
