package com.commuting.commutingapp.trip.service.api;

import com.commuting.commutingapp.trip.dto.trip.common.TripRequestResponseWithMatches;
import com.commuting.commutingapp.trip.dto.triprequest.CreateTripRequestDto;
import com.commuting.commutingapp.trip.dto.triprequest.TripRequestResponse;

import java.util.List;

public interface TripRequestService {
    String create(CreateTripRequestDto createTripRequestDto);

    List<TripRequestResponse> get();

    TripRequestResponseWithMatches get(String id);

    void delete(String id);
}
