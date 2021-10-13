package com.commuting.commutingapp.trip.dto.trip.generic;

import com.commuting.commutingapp.location.dto.location.response.LocationResponse;
import com.commuting.commutingapp.trip.model.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericPastTripResponse {
    private String tripId;
    private LocationResponse fromLocation;
    private LocationResponse toLocation;
    private String leaveTime;
    private String arriveTime;
    private boolean isDriver;
    private TripStatus status;
}
