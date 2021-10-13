package com.commuting.commutingapp.trip.service.api;

import com.commuting.commutingapp.trip.dto.statistic.StatisticsResponse;
import com.commuting.commutingapp.trip.dto.trip.TripAvailabilityRequest;

public interface StatisticsService {
    StatisticsResponse get();
}
