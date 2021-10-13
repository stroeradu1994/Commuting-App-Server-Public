package com.commuting.commutingapp.trip.dto.trip.common;

import com.commuting.commutingapp.location.dto.location.response.LocationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TripRequestResponseWithMatches {
    private String id;
    private LocationResponse fromLocation;
    private LocationResponse toLocation;
    private String dateTime;
    private List<MatchResponse> matches;
}
