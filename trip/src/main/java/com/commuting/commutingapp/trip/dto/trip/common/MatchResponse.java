package com.commuting.commutingapp.trip.dto.trip.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MatchResponse {
    private String matchId;
    private String driverId;
    private String driverName;
    private String tripId;
    private String carBrand;
    private String carModel;
    private String carColor;
    private double rating;
    private String polyline;
    private double pickupWalkingDistance;
    private double dropWalkingDistance;
    private String pickupPoint;
    private String dropPoint;
    private String leaveTime;
    private String arriveTime;
    private String pickupTime;
    private String dropTime;
}
