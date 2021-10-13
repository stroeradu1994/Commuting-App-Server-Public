package com.commuting.commutingapp.trip.service.api;

import com.commuting.commutingapp.trip.dto.statistic.RatingRequest;

public interface RatingService {
    void rate(RatingRequest ratingRequest);
}
