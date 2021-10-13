package com.commuting.commutingapp.trip.dto.trip.generic;

import com.commuting.commutingapp.location.dto.location.response.LocationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenericUpcomingTripResponse {
    private String tripId;
    private LocationResponse fromLocation;
    private LocationResponse toLocation;
    private String leaveTime;
    private String arriveTime;
    private int passengers;
    /*
    0 - driver
    1 - passenger
     */
    private boolean driver;
    private boolean confirmed;
}
