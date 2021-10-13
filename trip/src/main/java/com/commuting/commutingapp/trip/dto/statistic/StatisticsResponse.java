package com.commuting.commutingapp.trip.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class StatisticsResponse {
    double numberOfTripsAsDriver;
    double numberOfTripsAsPassenger;
    double kmAsDriver;
    double kmAsPassenger;
    double numberOfPassengers;
}
