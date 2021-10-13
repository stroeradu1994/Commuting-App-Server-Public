package com.commuting.commutingapp.trip.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingRequest {
    private String tripId;
    private int rate;
}
