package com.commuting.commutingapp.trip.dto.trip.generic;

import com.commuting.commutingapp.location.dto.location.response.LocationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericActiveTripResponse {
    private String tripId;
    private LocationResponse fromLocation;
    private LocationResponse toLocation;
    private String arriveTime;
    /*
    0 - driver
    1 - passenger
     */
    private boolean isDriver;

    // For Passenger
    private String pickUpDropTime;
    private boolean isNext;

    // For Driver
    private String nextStopPassengerName;

    // For Both
    private boolean isPickup;
}
