package com.commuting.commutingapp.trip.service.mapper;

import com.commuting.commutingapp.location.dto.location.internal.LocationPair;
import com.commuting.commutingapp.trip.dto.triprequest.CreateTripRequestDto;
import com.commuting.commutingapp.trip.dto.trip.common.TripRequestResponseWithMatches;
import com.commuting.commutingapp.trip.dto.triprequest.TripRequestResponse;
import com.commuting.commutingapp.trip.model.TripRequest;

public interface TripRequestMapper {

    TripRequest buildTripRequestForCreate(CreateTripRequestDto createTripRequestDto, LocationPair locationPair);

    TripRequestResponseWithMatches buildTripRequestResponseWithMatches(TripRequest tripRequest);

    TripRequestResponse buildTripRequestResponse(TripRequest tripRequest);
}
