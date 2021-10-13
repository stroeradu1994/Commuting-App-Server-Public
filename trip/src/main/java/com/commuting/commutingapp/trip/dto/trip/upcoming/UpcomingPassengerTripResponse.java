package com.commuting.commutingapp.trip.dto.trip.upcoming;

import com.commuting.commutingapp.location.dto.location.response.LocationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpcomingPassengerTripResponse {
    private String tripId;
    private String driverId;
    private String driverName;
    private double driverRating;
    private String carBrand;
    private String carModel;
    private String carColor;
    private LocationResponse fromLocation;
    private LocationResponse toLocation;
    private double pickupWalkingDistance;
    private double dropWalkingDistance;
    private String pickupPoint;
    private String dropPoint;
    private String leaveTime;
    private String arriveTime;
    private String pickupTime;
    private String dropTime;
    private String polyline;
    private boolean confirmed;
}
