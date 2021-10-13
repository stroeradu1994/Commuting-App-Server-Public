package com.commuting.commutingapp.trip.dto.triprequest;

import com.commuting.commutingapp.location.dto.location.response.LocationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TripRequestResponse {
    private String id;
    private LocationResponse fromLocation;
    private LocationResponse toLocation;
    private String dateTime;
    private int matches;
}
