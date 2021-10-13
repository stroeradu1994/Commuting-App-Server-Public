package com.commuting.commutingapp.trip.service.api;

import com.commuting.commutingapp.position.dto.PositionDto;

public interface TripPositionService {
    PositionDto get(String tripId, String userId);
}
