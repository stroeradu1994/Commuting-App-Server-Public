package com.commuting.commutingapp.trip.dto.trip.active;

import com.commuting.commutingapp.location.dto.location.response.LocationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivePassengerTripResponse {
    private String tripId;
    private String driverId;
    private String driverName;
    private double driverRating;
    private String carBrand;
    private String carModel;
    private String carColor;
    private String carPlate;
    private LocationResponse fromLocation;
    private LocationResponse toLocation;
    private boolean pickedUp;
    private boolean dropped;
    private boolean arrivedAtPickup;
    private double pickupWalkingDistance;
    private double dropWalkingDistance;
    double tripDistance;
    private String pickupPoint;
    private String pickupAddress;
    private String dropPoint;
    private String dropAddress;
    private String leaveTime;
    private String arriveTime;
    private String pickupTime;
    private String dropTime;
    private String polyline;
}
